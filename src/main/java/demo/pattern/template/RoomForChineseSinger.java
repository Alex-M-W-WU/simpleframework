package demo.pattern.template;

public class RoomForChineseSinger extends KTVRoom {

    @Override
    protected void orderSong() {
        System.out.println("中文歌曲。。。。");
    }
    @Override
    protected void orderExtra(){
        System.out.println("东西真好吃，吃出此号此次");
    }
}
