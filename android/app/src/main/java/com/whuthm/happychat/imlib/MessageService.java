package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.vo.LoadDataDirection;
import com.whuthm.happychat.imlib.model.Message;
import java.util.List;
import io.reactivex.Observable;

/**
 * Created by huangming on 18/07/2018.
 */

public interface MessageService {

    Observable<List<Message>> getHistoryMessages(String conversationId, long baseMessageId, LoadDataDirection direction, int count);

    Observable<Message> sendMessage(Message message);

    Observable<Message> resendMessage(Message message);


}
