package org.camilogo1200.rxjava3;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        //creatingObservables1();
        //creatingObservables2();

        try {
            connectableObservablesMulticasting();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        readLine();
    }

    private static void connectableObservablesMulticasting() throws InterruptedException {
        /*
        //cold Observable
        Observable<Long> obs = Observable.interval(2, TimeUnit.SECONDS);
        obs.subscribe(e -> {
            printStream("Subscriber 1",e);
        });
        Thread.sleep(10000);
        obs.subscribe(getLongConsumer());
        */
        /* Hot Observable */

        ConnectableObservable<Long> obs = Observable.interval(2, TimeUnit.SECONDS).publish();
        obs.connect();
        obs.subscribe(e -> printStream("Hot #1", e));
        Thread.sleep(6000);
        obs.subscribe(e -> printStream("Hot #2", e));
        Thread.sleep(6000);

    }

    private static Consumer<Long> getLongConsumer() {
        return e -> {
            printStream("Subscriber 2", e);
        };
    }

    private static void printStream(String streamName, Long e) {
        System.out.println(MessageFormat.format("Stream => {0} value - {1} ", streamName, e));
    }

    private static void creatingObservables2() {
        Observable<String> source = Observable.just("Orange", "Black", "Red", "Blue", "Green", "Yellow", "Pink");

        source.subscribe(App::handleSuccess, App::handleError, App::handleCompleted);
        source.subscribe(App::handleSuccess, App::handleError);
        source.subscribe(App::handleSuccess);

    }

    private static void handleCompleted() {
        System.out.println("completed!");
    }

    private static void handleError(Throwable e) {
        System.out.println("There was an error" + e.getMessage());
    }

    private static void handleSuccess(String color) {
        System.out.println("handleSuccess => " + color);
    }

    private static void readLine() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        try {
            bf.readLine();
        } catch (Exception ex) {
        }
    }

    private static void creatingObservables1() {
        Observable<String> hellObservable = Observable.create(
                (ObservableEmitter<String> e) -> {
                    for (int i = 0; i < 5; i++) {
                        System.out.println("Emitting " + i);
                        e.onNext("Hello " + i);
                    }
                    e.onComplete();
                });


        Observable<Integer> generateObservable = Observable.generate((Emitter<Integer> e) -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Emitting " + i);
                e.onNext(i);
            }
            e.onComplete();
        });

        Observable<String> observableJust = Observable.just("Hi", "There", "!");
        //from Iterable

        List<String> lElements = new ArrayList<>(List.of("hello", "from", "iterable", "!"));
        Observable<String> observableIterable = Observable.fromIterable(lElements);

        Observable<String> observableRange = Observable.range(3, 10)
                .filter(number -> number % 2 == 0)
                .map(number -> number + " :D map Operator!");

        Observable<String> observableInterval = Observable.interval(1, TimeUnit.MICROSECONDS).map(number -> ":D" + number);

        Observable<String> observableEmpty = Observable.empty();
        Observable<String> observableNever = Observable.never();
        Observable<String> observableError = Observable.error(new RuntimeException());
        Observable<String> observableDefer = Observable.defer(() -> {
            return Observable.fromIterable(lElements);
        });
        Observable<String> observableCallable = Observable.fromCallable(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Callable fnc => " + i);
            }
            return "Done! return from Callable!";
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("observer onSubscribe: disposed = " + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("observer onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("observer onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("observer onComplete!");
            }
        };

        // hellObservable.subscribe(observer);
        hellObservable.subscribe(observer);
        //observableJust.subscribe(observer);
        // observableIterable.subscribe(observer);
        //observableRange.subscribe(observer);
        //observableInterval.subscribe(observer);
        //observableEmpty.subscribe(observer);
        //observableNever.subscribe(observer);
        //observableError.subscribe(observer);
        //observableDefer.subscribe(observer);
        //lElements.add("New");
        //lElements.add("Elements");
        //observableDefer.subscribe(observer);
        //observableCallable.subscribe(observer);
    }
}
