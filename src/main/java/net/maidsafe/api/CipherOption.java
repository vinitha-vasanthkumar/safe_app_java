package net.maidsafe.api;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.CipherOpt;
import net.maidsafe.api.model.PublicEncryptKey;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.CipherOptBinding;
import net.maidsafe.utils.CallbackHelper;

public class CipherOption {
    private final App app;
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();
    private CipherOptBinding cipherOpt = BindingFactory.getInstance()
            .getCipherOpt();

    public CipherOption(App app) {
        this.app = app;
    }

    public CompletableFuture<CipherOpt> getPlain() {
        final CompletableFuture<CipherOpt> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        cipherOpt.cipher_opt_new_plaintext(app.getAppHandle(), Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new CipherOpt(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<CipherOpt> getSymmetric() {
        final CompletableFuture<CipherOpt> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        cipherOpt.cipher_opt_new_symmetric(app.getAppHandle(), Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new CipherOpt(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<CipherOpt> getAsymmetric(
            PublicEncryptKey publicEncryptKey) {
        final CompletableFuture<CipherOpt> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        cipherOpt.cipher_opt_new_asymmetric(app.getAppHandle(),
                publicEncryptKey.getHandle(), Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));
        cbFuture.thenAccept(handle -> future.complete(new CipherOpt(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

}
