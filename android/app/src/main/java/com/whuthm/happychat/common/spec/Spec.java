package com.whuthm.happychat.common.spec;

public interface Spec<T> {

    boolean isSatisfiedBy(T product);

}
