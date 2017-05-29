package net.maidsafe.api.model;

import java.util.concurrent.CompletableFuture;

import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CryptoBinding;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.FfiConstant;

import com.sun.jna.Pointer;

public class PublicEncryptKey {

    private final Pointer appHandle;
    private final long handle;
    private final CryptoBinding lib = BindingFactory.getInstance().getCrypto();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public PublicEncryptKey(Pointer appHandle, long handle) {
        this.appHandle = appHandle;
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }

    public CompletableFuture<byte[]> getRaw() {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        final CompletableFuture<Pointer> cbFuture = new CompletableFuture<>();
        lib.enc_pub_key_get(appHandle, handle, Pointer.NULL,
                callbackHelper.getPointerCallback(cbFuture));
        cbFuture.thenAccept(pointer -> {
            future.complete(pointer.getByteArray(0,
                    FfiConstant.BOX_PUBLICKEYBYTES));
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<byte[]> encrypt(byte[] data,
                                             SecretEncryptKey senderSecretKey) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        lib.encrypt(appHandle, data, data.length, handle,
                senderSecretKey.getHandle(), Pointer.NULL,
                callbackHelper.getDataCallback(future));

        return future;
    }

    public CompletableFuture<byte[]> encryptSealed(byte[] data) {
        final CompletableFuture<byte[]> future;
        future = new CompletableFuture<>();

        lib.encrypt_sealed_box(appHandle, data, data.length, handle,
                Pointer.NULL, callbackHelper.getDataCallback(future));

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lib.enc_pub_key_free(appHandle, handle, Pointer.NULL,
                callbackHelper.getResultCallBack(null));
    }

}
