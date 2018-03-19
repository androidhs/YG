package android.eq366pt.zxtnetwork.com.yg;

/**
 * Created by lbk on 2018/2/27.
 * 自动播放文件夹
 */

public class Autoplay {
    private String c;
    private String d;
    private String o;
    private String f;
    private int row;
    private int column;
    private int page;
    private Long time;

    public Autoplay(String c, int page) {
        this.c = c;
        this.page = page;
    }

    public Autoplay( String d, Long time) {
        this.d = d;
        this.time = time;
    }


    public Autoplay(String o, int row, int column) {
        this.o = o;
        this.row = row;
        this.column = column;
    }

    public Autoplay(String f, int row, int column, Long time) {
        this.f = f;
        this.row = row;
        this.column = column;
        this.time = time;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
