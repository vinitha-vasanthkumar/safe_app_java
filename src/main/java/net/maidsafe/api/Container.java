package net.maidsafe.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.sun.jna.Pointer;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;
import net.maidsafe.api.model.Permission;
import net.maidsafe.binding.AccessContainerBinding;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.utils.CallbackHelper;

public class Container {

    private final App app;
    private final AccessContainerBinding accessBinding = BindingFactory
            .getInstance().getAccessContainer();
    private final CallbackHelper callbackHelper = CallbackHelper.getInstance();

    public Container(App app) {
        this.app = app;
    }

    public CompletableFuture<List<String>> getContainers() {
        final CompletableFuture<List<String>> future;
        future = new CompletableFuture<>();
        accessBinding.access_container_get_names(app.getAppHandle(),
                Pointer.NULL, callbackHelper.getStringArrayCallback(future));
        return future;
    }

    public CompletableFuture<Void> refresh() {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        accessBinding.access_container_refresh_access_info(app.getAppHandle(),
                Pointer.NULL, callbackHelper.getResultCallBack(future));

        return future;
    }

    public CompletableFuture<MutableData> getContainer(String containerName) {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        accessBinding.access_container_get_container_mdata_info(
                app.getAppHandle(), containerName, Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    public CompletableFuture<MutableData> getAppContainer() {
        final CompletableFuture<MutableData> future;
        final CompletableFuture<Long> cbFuture;
        future = new CompletableFuture<>();
        cbFuture = new CompletableFuture<>();

        String appName = "apps/" + app.getAppInfo().getId();
        String scope = app.getAppInfo().getScope();
        if (scope != null && !scope.isEmpty()) {
            appName = "/" + scope;
        }

        accessBinding.access_container_get_container_mdata_info(
                app.getAppHandle(), appName, Pointer.NULL,
                callbackHelper.getHandleCallBack(cbFuture));

        cbFuture.thenAccept(handle -> future.complete(new MutableData(app.getAppHandle(), handle))).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<Boolean> hasAccess(final String containerName,
                                                List<Permission> permissions) {
        final CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
        final List<CompletableFuture<Boolean>> permResults = new ArrayList<>();
        permissions.forEach(permission -> permResults.add(hasAccess(containerName, permission)));
        CompletableFuture<?>[] temp = new CompletableFuture<?>[permResults
                .size()];
        CompletableFuture.allOf(permResults.toArray(temp))
                .thenAccept(v -> {
                    for (CompletableFuture<Boolean> permResult : permResults) {
                        try {
                            if (!permResult.get()) {
                                future.complete(false);
                                return;
                            }
                        } catch (InterruptedException e) {
                            future.completeExceptionally(e);
                        } catch (ExecutionException e) {
                            future.completeExceptionally(e);
                        }
                    }
                    future.complete(true);
                }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    private CompletableFuture<Boolean> hasAccess(String containerName,
                                                 Permission permission) {
        final CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
        accessBinding.access_container_is_permitted(app.getAppHandle(),
                containerName, permission.ordinal(), Pointer.NULL,
                callbackHelper.getBooleanCallback(future));
        return future;
    }

}
