package com.whuthm.happychat.service.vo;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Client 身份实体
 * domain : im, push
 */
public final class Identifier {

    private final String node;
    private final String domain;
    private final String resource;

    private String id;

    private Identifier(@NotNull String node, @NotNull String domain, @NotNull String resource) {
        if (StringUtils.isEmpty(node)) {
            throw new IllegalArgumentException("node is empty");
        }
        if (StringUtils.isEmpty(domain)) {
            throw new IllegalArgumentException("domain is empty");
        }
        if (StringUtils.isEmpty(resource)) {
            throw new IllegalArgumentException("resource is empty");
        }
        this.node = node;
        this.domain = domain;
        this.resource = resource;
    }

    public String getNode() {
        return node;
    }

    public String getDomain() {
        return domain;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Identifier) {
            Identifier o = (Identifier) obj;
            return Objects.equals(node, o.node)
                    && Objects.equals(domain, o.domain)
                    && Objects.equals(resource, o.resource);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, domain, resource);
    }

    private final String getId() {
        if (id == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(node)
                    .append("@")
                    .append(domain)
                    .append("/")
                    .append(resource);
            id = sb.toString();
        }
        return id;
    }

    public static Identifier from(String identifier) throws NullPointerException, IndexOutOfBoundsException, IllegalArgumentException {
        int nodeEndIndex = identifier.indexOf("@");
        int domainEndIndex = identifier.lastIndexOf("/");
        return new Identifier(
                identifier.substring(0, nodeEndIndex)
                , identifier.substring(nodeEndIndex + 1, domainEndIndex)
                , identifier.substring(domainEndIndex + 1));
    }

    public static Identifier from(@NotNull String node, @NotNull String domain, @NotNull String resource) throws IllegalArgumentException {
        return new Identifier(node, domain, resource);
    }

    @Override
    public String toString() {
        return getId();
    }

}
