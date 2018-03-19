package android.eq366pt.zxtnetwork.com.yg;

/**
 * Created by lbk on 2018/1/25.
 * keyLED 文件夹
 */

public class keyLED {
    private int page;
    private int row;
    private int column;
    private int repeat;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public keyLED(int page, int row, int column, int repeat) {
        this.page = page;
        this.row = row;
        this.column = column;
        this.repeat = repeat;
    }


}
