package io.github.yxsnake.pisces.web.core.utils.sm.model;

public class SM2KeyPair<U, V> {
    protected V privateKey;
    protected U publicKey;

    public SM2KeyPair(U publicKey, V privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public U getPublic() {
        return publicKey;
    }

    public V getPrivate() {
        return privateKey;
    }
}