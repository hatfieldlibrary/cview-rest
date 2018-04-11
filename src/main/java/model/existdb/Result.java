package model.existdb;
import java.util.ArrayList;
import java.util.List;
public class Result
{
    private String total;

    private List<Item> item;

    public void setTotal(String total){
        this.total = total;
    }
    public String getTotal(){
        return this.total;
    }
    public void setItem(List<Item> item){
        this.item = item;
    }
    public List<Item> getItem(){
        return this.item;
    }
}
