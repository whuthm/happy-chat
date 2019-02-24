package com.whuthm.happychat.common.spec;

import java.util.List;

public interface ProductRepository<T> {

    T getProductSatisfiedBy(Spec<T> spec);

    List<T> getAllProducts();

    List<T> getProductsSatisfiedBy(Spec<T> spec);

}
