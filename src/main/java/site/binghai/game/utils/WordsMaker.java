package site.binghai.game.utils;


/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public class WordsMaker {
    public static void main(String[] args) {
        String words = "重重选拔中脱颖而出的顶级选手要在校园十佳歌手决赛中与大家见面啦!&注意欧，我们的抢票时间是11月29日14点整哦~&抢票还未开始之前你可以在这里发送弹幕或者玩耍这段神奇的文字哟，不要忘记开声音~&至于为什么这么高端，别问为什么，就是任性！";
        char[] wds = words.toCharArray();
        int index = 0;
        boolean breakF = true;
        for (int i = 2; i < 13; i++) {
            for (int j = 1; j < 20 && breakF; j++) {
                if(index < wds.length){
                    char cc = wds[index];
                    if(cc == '&'){
                        breakF = false;
                    }else{
                        System.out.println("\""+cc+"\",\"\",\"\","+j+","+i+",");
                    }
                    index++;
                }
            }
            breakF = true;
        }
    }
}
