package com.whuthm.happychat.imlib;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.data.Constants;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 会话&消息服务
 *
 * Created by huangming on 18/07/2018.
 */

public class ConversationService {
    
    private static ConversationService sInstance;
    
    private ChatConnection mConnection;
    
    private Flowable<Message> mMessageFlowable;
    
    private FlowableEmitter<Message> mMessageEmitter;
    
    private Observable<Conversation> mConversationObservable;
    
    private ObservableEmitter<Conversation> mConversationEmitter;
    
    private ConversationService() {
        mMessageFlowable = Flowable.create(new FlowableOnSubscribe<Message>() {
            @Override
            public void subscribe(FlowableEmitter<Message> e) throws Exception {
                mMessageEmitter = e;
            }
        }, BackpressureStrategy.BUFFER);
        
        mConversationObservable = Observable
                .create(new ObservableOnSubscribe<Conversation>() {
                    @Override
                    public void subscribe(ObservableEmitter<Conversation> e)
                            throws Exception {
                        mConversationEmitter = e;
                    }
                });
        
        mConnection = new ChatConnection();
        mConnection.setMessageListener(new MessageListener());
        mConnection.connect();
    }
    
    public static ConversationService instance() {
        if (sInstance == null) {
            synchronized (ConversationService.class) {
                if (sInstance == null) {
                    sInstance = new ConversationService();
                }
            }
        }
        
        return sInstance;
    }
    
    public void subscribeMessage(Subscriber<Message> subscriber) {
        mMessageFlowable.subscribe(subscriber);
    }
    
    public Disposable subscribeMessage(Consumer<Message> next) {
        return mMessageFlowable.subscribe(next);
    }
    
    public void subscribeConversation(Observer<Conversation> observer) {
        mConversationObservable.subscribe(observer);
    }
    
    public Disposable subscribeConversation(Consumer<Conversation> next) {
        return mConversationObservable.subscribe(next);
    }
    
    // 检查当前会话是否存在
    private void checkConversation(final Message message) {
        Observable<List<Conversation>> observable = Observable
                .fromCallable(new Callable<List<Conversation>>() {
                    @Override
                    public List<Conversation> call() throws Exception {
                        return DBOperator.getConversations();
                    }
                });
        
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Conversation>>() {
                    @Override
                    public void accept(List<Conversation> conversations)
                            throws Exception {
                        boolean exists = false;
                        
                        for (Conversation conversation : conversations) {
                            if (message.getToUserId()
                                    .equals(conversation.getConversionId())) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            if (Constants.ConversationType.SingleChat
                                    .equals(message.getType())) {
                                addConversation(message);
                            }
                            else {
                                // TODO 拉取群组信息
                            }
                        }
                    }
                });
        
    }
    
    private void addConversation(Message message) {
        Conversation conversation = new Conversation();
        conversation.setConversionId(message.getToUserId());
        conversation.setConversionName(message.getFromUserId());
        conversation.setCreateTime(message.getTime());
        conversation.setConversationType(message.getType());
        DBOperator.addConversation(conversation);
        
        mConversationEmitter.onNext(conversation);
    }
    
    private class MessageListener implements IMessageListener {
        @Override
        public void onMessageReceived(Message message) {
            mMessageEmitter.onNext(message);
            
            checkConversation(message);
        }
        
        @Override
        public void onFinish(Throwable error) {
            Logs.w("onFinish " + error);
            
            if (error != null) {
                mMessageEmitter.onError(error);
            }
            else {
                mMessageEmitter.onComplete();
            }
        }
    }
}
