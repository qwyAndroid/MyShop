package shop.qwy.com.myshop.bean;

/**
 * created by qwyAndroid on 2016/9/25
 */
public class ShoppingCart extends Wears{

    private int count;
    private boolean isChecked=true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
