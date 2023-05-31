package org.example;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ReplyObserver<T> implements StreamObserver<T> {
    private boolean isCompleted=false;
    private boolean success=false;

    private Logger logger = Logger.getLogger(ReplyObserver.class.getName());
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
        logger.warning("Something went wrong getting answer from server.\n");
        System.out.println(t.getMessage());
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
