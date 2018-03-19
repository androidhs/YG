package android.eq366pt.zxtnetwork.com.yg;

/**
 * Created by lbk on 2018/2/5.
 */

public class KeySound {
    private int page;
    private int row;
    private int column;
    private String Path;

    public KeySound(int page, int row, int column, String path) {
        this.page = page;
        this.row = row;
        this.column = column;
        Path = path;
    }

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

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }
}
