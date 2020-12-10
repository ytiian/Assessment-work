import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Random;

public class FriedChickenTest {
    public static void main(String[] args){
        System.out.println("今天的日期是：2020年12月12日。开始工作吧！");//假设今天的日期为2020.12.12
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入餐厅账户余额（元）：");
        double Balance=scanner.nextDouble();
        West2FriedChickenRestaurant west2=new West2FriedChickenRestaurant(Balance);
        west2.Purchase();//采购
        west2.SellSetMeal(1);//套餐1
        west2.SellSetMeal(2);//套餐2
        west2.SellSetMeal(3);//套餐3
        west2.SellSetMeal(4);//套餐4
        System.out.println("余额:"+west2.GetBalance()+"元");
    }

}
class LocalDate{
    protected int ProductionYear;
    protected int ProductionMonth;//生产月
    protected int ProductionDay;//生产日
    protected int QualityGuaranteePeriod;//保质期
    LocalDate(int ProductionYear,int ProductionMonth,int ProductionDay,int QualityGuaranteePeriod){
        this.ProductionYear=ProductionYear;
        this.ProductionMonth=ProductionMonth;
        this.ProductionDay=ProductionDay;
        this.QualityGuaranteePeriod=QualityGuaranteePeriod;
    }
    public boolean IsOverdue(){
        return 12-ProductionDay>QualityGuaranteePeriod;//假设今天12日，并假设饮料都是12月生产（12日前）
    }
    public String GetLocalDate(){
        return ProductionYear+"."+ProductionMonth+"."+ProductionDay;
    }
}
abstract class Drinks{
    protected String DrinkName;
    protected double DrinkCost;
    protected LocalDate ProductionDate;
    Drinks(String DrinkName){
        this.DrinkName=DrinkName;
    }
    Drinks(String DrinkName,double DrinkCost,LocalDate ProductionDate){
        this.DrinkName = DrinkName;
        this.DrinkCost = DrinkCost;
        this.ProductionDate = ProductionDate;
    }
    public String GetName(){
        return DrinkName;
    }
    public abstract String toString();
}
class Beer extends Drinks{
    protected float AlcoholDegree;
    Beer(String DrinkName,double DrinkCost,LocalDate ProductionDate,float AlcoholDegree) {
        super(DrinkName,DrinkCost,ProductionDate);
        this.AlcoholDegree=AlcoholDegree;
    }
    Beer(String DrinkName,float AlcoholDegree){
        super(DrinkName);
        this.AlcoholDegree=AlcoholDegree;
    }
    public String toString(){
        return "啤酒："+DrinkName+"(酒精度数: "+AlcoholDegree*100+"%)";
    }
}
class Juice extends Drinks{
    Juice(String DrinkName,double DrinkCost,LocalDate ProductionDate) {
        super(DrinkName,DrinkCost,ProductionDate);
    }
    Juice(String DrinkName){
        super(DrinkName);
    }
    public String toString(){
        return "果汁："+DrinkName;
    }
}
class SetMeal{
    protected String PackageName;
    protected double PackagePrime;
    protected Drinks drink;
    protected String chicken;
    SetMeal(String PackageName,double PackagePrime,Drinks drink,String chicken){
        this.PackageName=PackageName;
        this.PackagePrime=PackagePrime;
        this.drink=drink;
        this.chicken=chicken;
    }
    public String GetName(){
        return PackageName;
    }
}
interface FriedChickenRestaurant{
    void SellSetMeal(int type);
    void Purchase();
}
class West2FriedChickenRestaurant implements FriedChickenRestaurant{
    protected double AccountBalance;
    protected Queue<Beer> BeerList;//便于取出和放回
    protected Queue<Juice> JuiceList;
    protected static List<SetMeal> SetMealList;//静态块不需要改变，用List能够按索引获取元素
    static{
        Beer beer1=new Beer("百威",0.036f);
        Beer beer2=new Beer("青岛",0.04f);
        Juice juice1=new Juice("橙汁");
        Juice juice2=new Juice("芒果汁");
        SetMealList=new ArrayList<>();
        SetMeal meal1=new SetMeal("套餐1", 35,beer1,"鸡翅");
        SetMeal meal2=new SetMeal("套餐2",30,beer2,"鸡排");
        SetMeal meal3=new SetMeal("套餐3",39,juice1,"鸡块");
        SetMeal meal4=new SetMeal("套餐4",50,juice2,"鸡腿");
        SetMealList.add(meal1);
        SetMealList.add(meal2);
        SetMealList.add(meal3);
        SetMealList.add(meal4);

    }
    West2FriedChickenRestaurant(double AccountBalance){
        BeerList=new LinkedList<>();
        JuiceList=new LinkedList<>();
        this.AccountBalance=AccountBalance;
    }
    double GetBalance(){
        return AccountBalance;
    }
    public boolean use(Beer beer){
        Beer temp;
        int len=BeerList.size();
        for(int i=0;i<len;i++){
            temp=BeerList.poll();
            if(temp==null)
                break;
            if(temp.ProductionDate.IsOverdue()){
                System.out.println("丢弃过期饮品:"+temp.GetName()+" "+temp.ProductionDate.GetLocalDate());
                continue;
            }
            if(temp.GetName().equals(beer.GetName())){
                System.out.println("取出"+beer.GetName()+":"+temp.ProductionDate.GetLocalDate());
                return true;
            }
            else{
                BeerList.offer(temp);
            }
        }
        try{
                throw new IngredientSortOutException(beer.GetName()+"已售完");
        }catch(IngredientSortOutException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean use(Juice juice){
        Juice temp;
        int len=JuiceList.size();
        for(int i=0;i<len;i++){
            temp=JuiceList.poll();
            if(temp==null)
                break;
            if(temp.ProductionDate.IsOverdue()){
                System.out.println("丢弃过期饮品："+temp.GetName()+" "+temp.ProductionDate.GetLocalDate());
                continue;
            }
            if(temp.GetName().equals(juice.GetName())){
                System.out.println("取出"+juice.GetName()+":"+temp.ProductionDate.GetLocalDate());
                return true;
            }
            else{
                JuiceList.offer(temp);
            }
        }
        try{
            throw new IngredientSortOutException(juice.GetName()+"已售完");
        }catch(IngredientSortOutException e){
            e.printStackTrace();
        }
        return false;
    }

   // @Override
    public void SellSetMeal(int type){
        SetMeal meal=SetMealList.get(type-1);
        Drinks drinks=meal.drink;
        System.out.println(meal.GetName()+"包含"+drinks.toString()+"+"+meal.chicken);
        if(drinks instanceof Beer){
            Beer beer=(Beer)drinks;
            if(use(beer)){
                System.out.println("恭喜！你赚了"+meal.PackagePrime+"元。");
                AccountBalance+= meal.PackagePrime;
            }
            else{
                System.out.println("出售失败。");
            }
        }
        if(drinks instanceof Juice){
            Juice juice=(Juice)drinks;
            if(use(juice)){
                System.out.println("恭喜！你赚了"+meal.PackagePrime+"元。");
                AccountBalance+= meal.PackagePrime;
            }
            else{
                System.out.println("出售失败。");
            }
        }

    }
    //@Override
    public void Purchase(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("采购开始。");
        System.out.println("请输入你想购买的饮品数量：");
        int sum=scanner.nextInt();
        for(int i=0;i<sum;i++) {
            System.out.println("请输入“啤酒”或“果汁”：");
            String type=scanner.next();
            if(type.equals("啤酒")){
                System.out.println("请输入“百威”或“青岛”");
                String name=scanner.next();
                int cost;
                float alcohol;
                if(name.equals("百威")){
                    cost=10;
                    alcohol=0.036f;
                }
                else{
                    cost=12;
                    alcohol=0.04f;
                }
                Random r=new Random();
                LocalDate date=new LocalDate(2020,12,r.nextInt(12)+1,30);
                Beer beer=new Beer(name,cost,date,alcohol);
                try{
                    if(AccountBalance<cost) {
                        double abs=cost-AccountBalance;
                        throw new OverdraftBalanceException("进货费用超出账户余额"+abs+"元，购买失败。");
                    }
                    else{
                        BeerList.offer(beer);
                        AccountBalance-=cost;
                        System.out.println(beer.DrinkName+"*1"+"花费"+cost+"元。");
                    }

                }catch(OverdraftBalanceException e){
                    e.printStackTrace();
                }
                    
            }
            else if(type.equals("果汁")){
                System.out.println("请输入“橙汁”或“芒果汁”：");
                String name=scanner.next();
                int cost;
                if(name.equals("橙汁")){
                    cost=7;
                }
                else cost=8;
                Random r=new Random();
                LocalDate date=new LocalDate(2020,12,r.nextInt(12)+1,2);
                Juice juice=new Juice(name,cost,date);
                try{
                    if(AccountBalance<cost) {
                        double abs=cost-AccountBalance;
                        throw new OverdraftBalanceException("进货费用超出账户余额"+abs+"元，购买失败。");
                    }
                    else{
                        JuiceList.offer(juice);
                        AccountBalance-=cost;
                        System.out.println(juice.DrinkName+"*1"+"花费"+cost+"元。");
                    }

                }catch(OverdraftBalanceException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
class IngredientSortOutException extends RuntimeException{
    IngredientSortOutException(String msg){
        super(msg);
    }
}
class OverdraftBalanceException extends RuntimeException{
    OverdraftBalanceException(String msg){
        super(msg);
    }
}