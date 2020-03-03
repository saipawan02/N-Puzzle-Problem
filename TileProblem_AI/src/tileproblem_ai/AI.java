
package tileproblem_ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AI{
    public static void main(String[] args) {
        new TileProblem_AI().solu();
    }
}

class TileProblem_AI {
    int t;
    
    ArrayList<state> frontier = new ArrayList<>();
    ArrayList<state> explored = new ArrayList<>();
    public void solu() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Select number of tiles: ");
        int num = sc.nextInt();
        t = (int)Math.sqrt(num);
        System.out.println(t);
        System.out.println("enter the Initial state: ");
       int[][] tiles =  new int[t][t];
       for(int i=0;i<t;i++){
           for(int j=0;j<t;j++){
               int c=sc.nextInt();
               tiles[i][j]= c;
           }
       }

        
        int par_init = 0;boolean check;
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                check=true;
                if(tiles[i][j]!=0){
                    for(int x=i;x<t;x++){
                        for(int y=0;y<t;y++){
                            if(check){
                                y=j;
                                check=false;
                            }
                            if(tiles[i][j]>tiles[x][y] && tiles[x][y]!=0)
                                par_init+=1;
                        }
                    }
                }
            }
        }
        System.out.println(par_init);
        System.out.println("select the goal state"); 
       int[][] goal =  new int[t][t];
       for(int i=0;i<t;i++){
           for(int j=0;j<t;j++){
               goal[i][j]=sc.nextInt();//g[k++];
           }
       }
        
        int par_goal = 0;
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                check=true;
                if(goal[i][j]!=0){
                    for(int x=i;x<t;x++){
                        for(int y=0;y<t;y++){
                            if(check){
                                y=j;
                                check=false;
                            }
                            if(goal[i][j]>goal[x][y] && goal[x][y]!=0)
                                par_goal+=1;
                        }
                    }
                }
            }
        }
        System.out.println(par_goal);
        if(par_goal%2 != par_init%2){
            System.out.println("Parity dosen't match.");
            return ;
        }
        
        int hx=0,hy=0;
        int h2=0;
        char p;
            
        int cost = cost(tiles,goal);
        frontier.add(new state(tiles,cost,h2,'A'));
        
        
        while(!frontier.isEmpty()){
            System.out.println("\nArrayList size: "+frontier.size()+"\nArrayList content: ");
            System.out.println("");
            
            while(true){
                state s = findMin(frontier);//frontier.remove(0);  //also check collenctions.sort();
                tiles = s.getState();
                cost=s.getcost();
                if(!isDublicate(tiles,explored,cost)){
                    explored.add(s);
                    h2= s.k + 1;
                    p=s.getPosition();
                    break;
                }
            }
            
            System.out.println("");
            if(cost==0){
                System.out.println("solution found");
                System.out.println("**** Goal State Found ****");
                display(tiles);
                System.out.println("**************************");
                break;
            }
            
            System.out.println("##### Selected State ######");
            display(tiles);
            System.out.println("###########################");
            
            System.out.println("\nExplored size: "+explored.size()+"\nArrayList content: ");
            System.out.println("");
            for(int i=0;i<t;i++){
                for(int j=0;j<t;j++){
                    if(tiles[i][j]==0){
                        hx=i;hy=j;
                        i=t;
                        break;
                    }
                }
            }
            int[][] tiles1;
            int co;
            if(p !='R'){
                tiles1 = left_shift(tiles, hx, hy);
                if(tiles1!=null){
                    co=cost(tiles1, goal);
                    if(!(isDublicate(tiles1,explored,co) || isDublicate(tiles1, frontier,co))){
                        System.out.println("---------left--------");
                        display(tiles1);
                        System.out.println("---------------------");
                        frontier.add(0,new state(tiles1, co, h2,'L'));
                    }
                }
            }
            
            if(p !='L'){
                tiles1 = right_shift(tiles, hx, hy);
                if(tiles1!=null){
                    co=cost(tiles1, goal);
                    if(!(isDublicate(tiles1,explored,co) || isDublicate(tiles1, frontier,co))){
                        System.out.println("---------right-------");
                        display(tiles1);
                        System.out.println("---------------------");
                        frontier.add(0,new state(tiles1, co, h2,'R'));
                    }
                }
            }
            
            if(p !='D'){
                tiles1 = top_shift(tiles, hx, hy);
                if(tiles1!=null){
                    co=cost(tiles1, goal);
                    if(!(isDublicate(tiles1,explored,co) || isDublicate(tiles1, frontier,co))){
                        System.out.println("----------top--------");
                        display(tiles1);
                        System.out.println("---------------------");
                        frontier.add(0,new state(tiles1, co, h2,'T'));
                    }
                }
            }
            
            if(p !='T'){
                tiles1 = down_shift(tiles, hx, hy);
                if(tiles1!=null){
                    co=cost(tiles1, goal);
                    if(!(isDublicate(tiles1,explored,co) || isDublicate(tiles1, frontier,co))){
                        System.out.println("---------down--------");
                        display(tiles1);
                        System.out.println("---------------------");
                        frontier.add(0,new state(tiles1, co, h2,'D'));
                    }
                }
            }
            
            //Collections.sort(frontier);
        }
    }
    
    private state findMin(ArrayList<state> frontier) {
        int cost1=frontier.get(0).cost;
        int h1=frontier.get(0).getcost();
        int k=0;
        for(int i=0;i<frontier.size();i++){
            state s =frontier.get(i);
            if(cost1==s.cost){
                if(h1>s.getcost()){
                    cost1=s.cost;
                    k=i;
                }
            }else if(cost1>s.cost){
                cost1=s.cost;
                k=i;
            }
        }
        return frontier.remove(k);
    }
    
    boolean isDublicate(int[][] tiles,ArrayList<state> list,int cost){
        if(list.isEmpty())
            return false;
        for(int k=0;k<list.size();k++){
            state s = list.get(k);
            if(s.getcost()==cost){
                int temp1=1;
                int temp[][] = s.getState();
                for(int i=0;i<t;i++){
                    for(int j=0;j<t;j++){
                        if(temp[i][j]!=tiles[i][j]){
                            temp1=0;
                            i=t;
                            break;
                        }
                    }
                }
                if(temp1==1){
                    return true;
                }
            }
        }
        return false;
    }
    
    //------------------------------------------------------
    int[][] top_shift(int[][] tiles,int hx,int hy){
        int tiles1[][] = new int[t][t];
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                tiles1[i][j]=tiles[i][j];
            }
        }
        if(hx!=0){
            tiles1[hx][hy]=tiles1[hx-1][hy];
            tiles1[hx-1][hy]=0;
        }else{return null;}
        return tiles1;
    }
    int[][] down_shift(int[][] tiles,int hx,int hy){
        int tiles1[][] = new int[t][t];
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                tiles1[i][j]=tiles[i][j];
            }
        }
        if(hx!=t-1){
            tiles1[hx][hy]=tiles1[hx+1][hy];
            tiles1[hx+1][hy]=0;
        }else{return null;}
        return tiles1;
    }
    int[][] left_shift(int[][] tiles,int hx,int hy){
        int tiles1[][] = new int[t][t];
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                tiles1[i][j]=tiles[i][j];
            }
        }
        if(hy!=0){
            tiles1[hx][hy]=tiles1[hx][hy-1];
            tiles1[hx][hy-1]=0;
        }else{return null;}
        return tiles1;
    }
    int[][] right_shift(int[][] tiles,int hx,int hy){
        int tiles1[][] = new int[t][t];
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                tiles1[i][j]=tiles[i][j];
            }
        }
        if(hy!=t-1){
            tiles1[hx][hy]=tiles1[hx][hy+1];
            tiles1[hx][hy+1]=0;
        }else{return null;}
        return tiles1;
    }
    int cost(int[][] tiles,int[][] goal){
        int cost=0;
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                if(tiles[i][j]!=goal[i][j] && goal[i][j]!=0){
                    for(int x=0;x<t;x++){
                        for(int y=0;y<t;y++){
                            if(tiles[x][y]==goal[i][j]){
                                cost += ( (Math.abs(x-i)+Math.abs(y-j)) );
                            }
                        }
                    }
                }
            }
        }
        return cost;
    };
    
    void display(int[][] tiles){
        
        for(int i=0;i<t;i++){
            System.out.print("\t");
            for(int j=0;j<t;j++){
                System.out.print(tiles[i][j]+" ");
            }
            System.out.println("");
        }
    }

    
}

class state implements Comparable<state>{
    int cost;
    int k;
    int tiles[][] ;
    char p;
    state(int[][] tiles,int cost,int k,char p){
        this.tiles=tiles;
        this.k=k;
        this.cost=cost+k;
        this.p=p;
        System.out.println("cost: "+this.cost);
    }
    
    char getPosition(){
        return p;
    }
    
    int[][] getState(){
        return tiles;
    }
    int getcost(){
        return cost-k;
    }
    
    @Override
    public int compareTo(state s){
         if(this.cost==s.cost)
             if((this.cost-this.k)==(s.cost-s.k))
                return 0;
             else if((this.cost-this.k)>(s.cost-s.k))
                 return 1;
             else
                 return -1;
         else if(this.cost>s.cost)  
            return 1;  
         else  
            return -1;
    }
}
