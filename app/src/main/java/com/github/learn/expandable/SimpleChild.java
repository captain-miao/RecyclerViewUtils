package com.github.learn.expandable;

public class SimpleChild {

    private String mContent;
    private boolean mSolved;

    public SimpleChild(String content, boolean solved) {
        mContent = content;
        mSolved = solved;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
