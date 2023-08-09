package com.SoftTech.SelfParkingLot_RestApi.security;

import lombok.Data;

import java.util.*;

@Data
public class CurrentTokens {
    private TokenQueue queue=new TokenQueue();  //çift yönlü dairesel sıra
    private HashMap<String,String> hashMap = new HashMap<>();
    private Timer timer = new Timer();  // farklı bir thread için...
    private int tokenDuration;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            killDeadTokens();
            System.out.println("[!] Olu tokenler kontrol edildi.");
        }
    };

    public CurrentTokens(int tokenDuration) {
        timer.schedule(task, 10000, 10000);
        this.tokenDuration=tokenDuration;

    }

    public void add(String token,String userName){
        if(hashMap.containsKey(userName)){
            //bunu yapmasam kötü niyetli bir kullanıcı ram i şişirebilir...
            killAnyNodeFromQueue(userName);
        }
        addToQueue(token,userName);
        hashMap.put(userName,token);
    }

    public String remove(String userName){
        hashMap.remove(userName);
        killAnyNodeFromQueue(userName);
        System.out.println("[!] kullanıcı çıkış yaptı: "+userName);
        return "Çıkış başarılı.";
    }

    public HashMap<String, String> getTokens() {
        return hashMap;
    }

    private void addToQueue(String token,String userName){
        if(queue.getData()==null){
            //ilk token
            queue.setData(token);
            queue.setUserName(userName);
            queue.setInitialDate(new Date(System.currentTimeMillis()));
            queue.next=queue;
            queue.prev=queue;
        }else if(queue.prev==queue){
            //tek token var
            TokenQueue iter=new TokenQueue(token,userName);
            queue.next=iter;
            queue.prev=iter;
            iter.prev=queue;
            iter.next=queue;
        }else{
            //en az 2 token var
            TokenQueue iter=new TokenQueue(token,userName);
            queue.prev.next=iter;
            iter.prev=queue.prev;
            queue.prev=iter;
            iter.next=queue;
        }
    }


    private void removeFromQueue(){
        if(queue.getData()!=null){
            // 0 token degil...
            if(queue.next==queue){
                // 1 token...
                queue.setInitialDate(null);
                queue.setData(null);
                queue.setUserName(null);
                queue.next=null;
                queue.prev=null;
            }else{
                // 1 den fazla token...
                TokenQueue iter=queue.next;
                queue.prev.next=iter;
                iter.prev=queue.prev;
                queue=iter;
            }
        }
    }

    private void killAnyNodeFromQueue(String userName){
        if(queue.getData()!=null){
            // en az 1 dugum var
            if(queue.getUserName().equals(userName)){
                removeFromQueue();
            }else{
                // 1 den fazla dugum olmalı ve silinmesi gereken dugum ilk dugum degil
                TokenQueue iter=queue;
                while(!iter.getUserName().equals(userName) || iter.next!=queue){
                    iter=iter.next;
                }
                if(iter.getUserName().equals(userName)){
                    iter.prev.next=iter.next;
                    iter.next.prev=iter.prev;
                }else{
                    System.out.println("[-] algoritma hatası: CurrentsTokens sınıfını takip et.");
                }
            }
        }
    }

    private void killDeadTokens(){
        Date killTime = new Date(System.currentTimeMillis()-tokenDuration);
        if(queue.getInitialDate()!=null && queue.getInitialDate().before(killTime)){
            //kuyrugun basını kontrol et burdan silinen olursa hashmap den de sil
            System.out.println("[!] Silinen token: "+queue.getData());
            hashMap.remove(queue.getUserName());
            removeFromQueue();
            killDeadTokens();   //bir sonrakini de kontrol et
        }
    }

    public List<String> listQueue(){
        List<String> listQueue = new ArrayList<>();
        if(queue.getData()!=null){
            TokenQueue iter = queue;
            listQueue.add(queue.getData());
            while(iter.next!=queue){
                iter=iter.next;
                listQueue.add(iter.getData());
            }
        }
        return listQueue;
    }

    public String  getTokenOfUser(String username){
        if(hashMap.containsKey(username)){
            return hashMap.get(username);
        }
        return "";
    }

}
