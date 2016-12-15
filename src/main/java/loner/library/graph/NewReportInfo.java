package loner.library.graph;

/**
 * Created by loner on 2016/10/26.
 */

public class NewReportInfo {

    private int month;
    private int completeAmount;
    private int overTimeAmount;
    private int exceptionAmount;

    public NewReportInfo(int month, int completeAmount, int exceptionAmount, int overTimeAmount) {
        this.completeAmount = completeAmount;
        this.exceptionAmount = exceptionAmount;
        this.month = month;
        this.overTimeAmount = overTimeAmount;
    }

    public void setCompleteAmount(int completeAmount) {
        this.completeAmount = completeAmount;
    }

    public void setExceptionAmount(int exceptionAmount) {
        this.exceptionAmount = exceptionAmount;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setOverTimeAmount(int overTimeAmount) {
        this.overTimeAmount = overTimeAmount;
    }

    public int getCompleteAmount() {

        return completeAmount;
    }

    public int getExceptionAmount() {
        return exceptionAmount;
    }

    public int getMonth() {
        return month;
    }

    public int getOverTimeAmount() {
        return overTimeAmount;
    }
}
