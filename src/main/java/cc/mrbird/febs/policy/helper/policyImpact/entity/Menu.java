package cc.mrbird.febs.policy.helper.policyImpact.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 政策文章的目录结构
 */
@Data
public class Menu {
    private String name;
    private List<Menu> child;
    public void addChild(Menu childMenu){
        if(this.child==null){
            this.child=new ArrayList<Menu>();
        }
        this.child.add(childMenu);
    }
}

class Test{
    public static void main(String[] args){
        Menu menu = new Menu();
        menu.setName("政策目标");
        Menu child = new Menu();
        child.setName("创新");
        menu.addChild(child);
        printMenu(menu);
    }
    public static void printMenu(Menu menu) {
        System.out.println(menu.getName());
        if (menu.getChild() != null) {
            for (Menu m : menu.getChild()) {
                printMenu(m);
            }
        }
    }
}