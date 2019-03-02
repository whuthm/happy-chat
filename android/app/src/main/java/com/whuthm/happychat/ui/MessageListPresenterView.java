package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.LoadDataDirection;

import java.util.List;

public interface MessageListPresenterView {

    void onHistoryMessagesLoadStarted(LoadDataDirection direction);

    void onHistoryMessagesLoaded(LoadDataDirection direction, List<Message> messages);

    void onHistoryMessagesLoadFailed(LoadDataDirection direction, Throwable tr);

    void onMessagesAdded(Message message);

    void onMessagesUpdated(Message message);

    void onMessagesRemoved(Message message);

}
