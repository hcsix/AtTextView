package cn.hclee.attextview.bean;

/**
 * Created by lee on 2017/5/1.
 */

public class AtBean {
    //文字
    private String name;
    //开始的位置
    private int startPos;
    //结束的位置
    private int endPos;

    public AtBean(String name, int startPos, int endPos) {
        this.name = name;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    @Override
    public String toString() {
        return "name --> " + name + "  startPos --> " + startPos + "  endPos --> " + endPos;
    }
}
