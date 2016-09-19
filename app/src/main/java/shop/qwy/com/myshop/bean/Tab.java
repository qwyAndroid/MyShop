package shop.qwy.com.myshop.bean;

/**
 * 作者：仇伟阳
 */
public class Tab {
    private int icon;
    private int tittle;
    private Class fragment;

    public Tab(int icon, int tittle, Class fragment) {
        this.icon = icon;
        this.tittle = tittle;
        this.fragment = fragment;
    }

    public int getTittle() {
        return tittle;
    }

    public void setTittle(int tittle) {
        this.tittle = tittle;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
