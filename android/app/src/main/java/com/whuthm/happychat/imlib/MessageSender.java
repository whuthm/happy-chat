package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.util.PacketUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;

class MessageSender implements IQPacketHandler {

    private final PacketSender packetSender;
    private final Scheduler subscribeOn;

    private Map<String, Message> sendingMessages;

    MessageSender(PacketSender packetSender, Scheduler subscribeOn) {
        this.packetSender = packetSender;
        this.subscribeOn = subscribeOn;
        this.sendingMessages = new ConcurrentHashMap<>();
    }

    private void sendMessageInternal(Message message) throws Exception {
        try {
            MessageProtos.MessageBean messageBean = MessageUtils.getSendMessagePacketFrom(message);
            this.sendingMessages.put(message.getId(), message);
            packetSender.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.message, messageBean));
            synchronized (message) {
                if (this.sendingMessages.containsKey(message.getId())) {
                    message.wait(8 * 1000);
                }
                if (message.getSid() == null) {
                    throw new Exception("Message sent Failed(" + message.getId() + ")");
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.sendingMessages.remove(message.getId());
        }
    }

    Observable<Message> sendMessage(final Message message) {
        return Observable
                .create(new ObservableOnSubscribe<Message>() {
                    @Override
                    public void subscribe(ObservableEmitter<Message> e) throws Exception {
                        sendMessageInternal(message);
                        e.onNext(message);
                        e.onComplete();
                    }
                })
                .subscribeOn(subscribeOn);
    }

    @Override
    public void handlerIQPacket(PacketSender packetSender, PacketProtos.Packet packet, IQProtos.IQ iq) throws Exception {
        if (iq.getAction() == IQProtos.IQ.Action.messageDelivered) {
            IQProtos.MessageDeliveredIQ messageDeliveredIQ = IQProtos.MessageDeliveredIQ.newBuilder().mergeFrom(iq.getData()).build();
            final Message message = sendingMessages.get(messageDeliveredIQ.getId());
            if (message != null) {
                synchronized (message) {
                    message.setSid(messageDeliveredIQ.getSid());
                    message.notifyAll();
                }
            }
        }
    }
}
