package loner.library.graph;

/**
 * Created by loner on 2016/10/26.
 */

public class GraphPaint {

    private int background;
    private int complete;
    private int error;

    public GraphPaint(int background, int complete, int error) {
        this.background = background;
        this.complete = complete;
        this.error = error;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getBackground() {
        return background;
    }

    public int getComplete() {
        return complete;
    }

    public int getError() {
        return error;
    }
}
