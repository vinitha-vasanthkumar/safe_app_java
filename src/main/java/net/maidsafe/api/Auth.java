package net.maidsafe.api;

import net.maidsafe.api.model.*;
import net.maidsafe.binding.AuthBinding;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.model.AppExchangeInfo;
import net.maidsafe.binding.model.AuthReq;
import net.maidsafe.binding.model.AuthUriResponse;
import net.maidsafe.binding.model.FfiContainerPermission;
import net.maidsafe.binding.model.ContainerRequest;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.utils.CallbackHelper;
import net.maidsafe.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Auth {

    private CallbackHelper callbackHelper = CallbackHelper.getInstance();
    private AuthBinding authBinding = BindingFactory.getInstance().getAuth();

    public CompletableFuture<String> getURI(AppInfo appInfo,
                                            List<ContainerPermission> permissions) {
        return this.getURI(appInfo, permissions, false);
    }

    public CompletableFuture<String> getURI(AppInfo appInfo,
                                            List<ContainerPermission> permissions, boolean createAppContainer) {
        final CompletableFuture<String> future;
        final CompletableFuture<AuthUriResponse> cbFuture;
        final FfiCallback.Auth callback;
        AppExchangeInfo appExchangeInfo;
        AuthReq request;
        future = new CompletableFuture<>();

        if (appInfo == null) {
            future.completeExceptionally(new Exception(CustomError.ARG_CAN_NOT_BE_NULL));
            return future;
        }

        if (permissions == null) {
            permissions = new ArrayList<>();
        }

        cbFuture = new CompletableFuture<>();

        callback = callbackHelper.getAuthCallback(cbFuture);
        appExchangeInfo = new AppExchangeInfo(appInfo);
        request = new AuthReq(appExchangeInfo, permissions,
                createAppContainer);
        authBinding
                .encode_auth_req(request, null, callback);

        cbFuture.thenAccept((res) -> future.complete(res.getUri())).exceptionally((t) -> {
            future.completeExceptionally(t);
            return null;
        });

        return future;
    }

    public CompletableFuture<String> getContainerRequestURI(AppInfo appInfo,
                                                            List<ContainerPermission> permissions) {
        AppExchangeInfo appExchangeInfo;
        List<FfiContainerPermission> contPermissions;
        FfiCallback.Auth callback;
        final CompletableFuture<String> future;

        future = new CompletableFuture<>();

        if (permissions == null || permissions.isEmpty()) {
            future.completeExceptionally(new Exception(
                    CustomError.CONTAINERS_CAN_NOT_BE_EMPTY));
            return future;
        }

        appExchangeInfo = new AppExchangeInfo(appInfo);
        contPermissions = new ArrayList<>();

        for (ContainerPermission permission : permissions) {
            contPermissions.add(new FfiContainerPermission(permission));
        }
        callback = (userData, result, reqId, uri) -> {
            if (result.isError()) {
                future.completeExceptionally(new Exception(result));
                return;
            }
            future.complete(uri);
        };

        BindingFactory
                .getInstance()
                .getAuth()
                .encode_containers_req(
                        new ContainerRequest(appExchangeInfo, contPermissions),
                        null, callback);
        return future;
    }

    public CompletableFuture<SafeClient> connectAsAnnonymous(
            final NetworkObserver observer) {
        final CompletableFuture<SafeClient> future;
        future = observer.getFuture();
        observer.setApp(new App(null, null, null));
        BindingFactory
                .getInstance()
                .getAuth()
                .app_unregistered(null, observer.getObserver(),
                        observer.getAppRef());
        if (Helper.isMockEnvironment()) {
            observer.getObserver().onResponse(null, 0, 0);
        }
        return future;
    }

    public CompletableFuture<SafeClient> connectWithURI(final AppInfo appInfo,
                                                        final String uri, final NetworkObserver observer) {
        final CompletableFuture<SafeClient> future;
        future = observer.getFuture();

        FfiCallback.AuthGranted authCb = (userData, reqId, authGranted) -> {
            observer.setApp(new App(appInfo,
                    new Keys(authGranted.app_keys),
                    new AccessContainerMeta(authGranted.access_container)));
            try {
                BindingFactory
                        .getInstance()
                        .getAuth()
                        .app_registered(appInfo.getId(), authGranted, null,
                                observer.getObserver(),
                                observer.getAppRef());
                if (Helper.isMockEnvironment()) {
                    observer.getObserver().onResponse(null, 0, 0);
                }
            } catch (NullPointerException e) {
                future.completeExceptionally(e);
            }
        };

        FfiCallback.ReqIdCallback containerCb = (userData, reqId) -> future.completeExceptionally(new Exception(
                CustomError.NOT_VALID_AUTH_URI));

        FfiCallback.NoArgCallback revokedCb = userData -> future.completeExceptionally(new Exception(
                CustomError.NOT_VALID_AUTH_URI));

        FfiCallback.ErrorCallback errCb = (userData, result, reqId) -> future.completeExceptionally(new Exception(result));

        BindingFactory
                .getInstance()
                .getAuth()
                .decode_ipc_msg(uri, null, authCb, containerCb, revokedCb,
                        errCb);

        return future;
    }

}
