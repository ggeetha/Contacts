package com.example.contacts.networkresource;

import android.util.Log;
import com.example.contacts.vo.Resource;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public abstract class NetworkBoundResource <LocalType, LocalView,  RemoteType> {

    private final String LOG_TAG = NetworkBoundResource.class.getSimpleName();


    public NetworkBoundResource(FlowableEmitter<Resource<LocalView>> emitter) {
        this.run(emitter);
    }

    public NetworkBoundResource()
    {

    }

    private void run(FlowableEmitter<Resource<LocalView>> emitter)
    {
        Log.i(LOG_TAG, "NBResource called");
        Disposable firstDataDisposable = getLocal().map(Resource::loading).subscribe(emitter::onNext);

        if(shouldFetch(null)) {
            getRemote().map(mapper())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(localTypeData -> {
                        firstDataDisposable.dispose();
                        //Log.i(LOG_TAG, "shouldFetch called: " + localTypeData);
                        saveCallResult(localTypeData);
                        getLocal().map(Resource::success).subscribe(emitter::onNext);
                    }, throwable -> {
                        //Log.e("NetworkBoundResource", throwable.getLocalizedMessage(), throwable);
                        //firstDataDisposable.dispose();
                        emitter.onError(throwable);
                    });
        }

    }

    public abstract Single<RemoteType> getRemote();

    public abstract Flowable<LocalView> getLocal();

    public abstract void saveCallResult(LocalType data);

    public abstract Function<RemoteType, LocalType> mapper();

    public abstract boolean shouldFetch(RemoteType data);
}
