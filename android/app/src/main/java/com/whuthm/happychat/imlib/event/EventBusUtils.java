package com.whuthm.happychat.imlib.event;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {


    public static void safeRegister(Object subscriber) {
        try  {
            EventBus.getDefault().register(subscriber);
        } catch (Exception ignored) {
        }
    }

    public static void safeUnregister(Object subscriber) {
        try  {
            EventBus.getDefault().unregister(subscriber);
        } catch (Exception ignored) {
        }
    }

    public static void safePost(Object event) {
        try  {
            EventBus.getDefault().post(event);
        } catch (Exception ignored) {
        }
    }

}
