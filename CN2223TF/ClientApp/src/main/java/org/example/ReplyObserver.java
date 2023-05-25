package org.example;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class ReplyObserver<T> implements StreamObserver<T> {
    private boolean isCompleted=false;
    private boolean success=false;
    public boolean OnSuccess() { return success; }
    public boolean isCompleted() { return isCompleted; }
    private final ArrayList<T> replies = new ArrayList<T>();

    @Override
    public void onNext(T value) {
        replies.add(value);
    }

    @Override
    public void onError(Throwable t) {
        isCompleted = true;
        System.out.println("Something went wrong getting answer from server.\n");
        throw new RuntimeException(t);
    }

    @Override
    public void onCompleted() {
        isCompleted = true;
        success = true;
    }

    public ArrayList<T> getReplies() {
        return replies;
    }
}
