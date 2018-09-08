package com.whuthm.happychat.common.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpecUtils {


    public static <T> List<T> getSatisfiedProducts(Collection<T> products, Spec<T> spec) {
        final List<T> satisfiedProducts = new ArrayList<>();
        for (T product : products) {
            if (spec.isSatisfiedBy(product)) {
                satisfiedProducts.add(product);
            }
        }
        return satisfiedProducts;
    }

}
