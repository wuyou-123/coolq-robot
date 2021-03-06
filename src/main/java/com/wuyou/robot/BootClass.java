package com.wuyou.robot;

import com.wuyou.service.StatService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.WebCookiesUtils;
import love.forte.common.ioc.annotation.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


/**
 * @author Administrator<br>
 * 2020年5月2日
 */
@Beans
@Component
public class BootClass {

    private final StatService service;

    @Autowired
    public BootClass(StatService service) {
        this.service = service;
    }

    public void boot() {
        GlobalVariable.BOOT_MAP.putAll(service.getAllStat());
        GlobalVariable.ADMINISTRATOR.add("1097810498");
        InputStream is2 = this.getClass().getResourceAsStream("/resources/kouzi.txt");
        if (is2 == null) {
            is2 = this.getClass().getClassLoader().getResourceAsStream("kouzi.txt");
        }
        StringBuilder kouzi = new StringBuilder();
        try {
            assert is2 != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is2, StandardCharsets.UTF_8))) {
                String str;
                while ((str = reader.readLine()) != null) {
                    kouzi.append(str).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlobalVariable.THREAD_POOL.execute(this::initLandlords);
        GlobalVariable.WEB_COOKIE.clear();
        GlobalVariable.WEB_COOKIE.putAll(WebCookiesUtils.getCookies());
        GlobalVariable.KOUZI.append(kouzi.toString());
///        setu();

    }

    public void initLandlords() {
        System.out.println("启动斗地主服务");
        GlobalVariable.resetLandlords();
//        if (GlobalVariable.BOOTSTRAP != null) {
//            GlobalVariable.PARENT_GROUP.shutdownGracefully();
//            GlobalVariable.CHILD_GROUP.shutdownGracefully();
//            GlobalVariable.BOOTSTRAP.bind().player().close();
//        }
//        GlobalVariable.PARENT_GROUP = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
//        GlobalVariable.CHILD_GROUP = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
//        GlobalVariable.BOOTSTRAP = new ServerBootstrap()
//                .group(GlobalVariable.PARENT_GROUP, GlobalVariable.CHILD_GROUP)
//                .player(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
//                .localAddress(new InetSocketAddress(ServerContains.port))
//                .childHandler(new DefaultChannelInitializer());
//
//        try {
//            ChannelFuture f = GlobalVariable.BOOTSTRAP.bind().sync();
//
//            SimplePrinter.serverLog("斗地主服务器已经在端口: " + ServerContains.port + " 启动成功");
//            //Init robot.
//            RobotDecisionMakers.init();
//            GlobalVariable.THREAD_POOL.execute(() ->
//                Executors.newScheduledThreadPool(5).scheduleAtFixedRate(RoomClearTask::new, 0, 20, TimeUnit.MINUTES)
//            );
//            f.player().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            GlobalVariable.BOOTSTRAP.config().group().shutdownGracefully();
//            GlobalVariable.BOOTSTRAP.config().childGroup().shutdownGracefully();
//        }
    }
///    public void setu() {
///        BlockingQueue<String> setu = GlobalVariable.setuQueue;
///        BlockingQueue<String> setuR18 = GlobalVariable.setuR18Queue;
///
///        new Thread(() -> {
///            while (true) {
///                System.out.println("开始获取R18图片");
///                long start = System.currentTimeMillis();
///                try {
///                    JSONObject json = getJson("1");
///                    if (json == null) {
///                        json = getJson("1");
///                    }
///                    System.out.println("获取到json数据: " + json);
///                    if (json.getInteger("code") == 0) {
///                        JSONObject data = JSONObject.parseObject(json.getJSONArray("data").getString(0));
///                        String url = data.getString("url");
///                        String fileName = "R18-" + data.getString("pid");
///                        System.out.println("准备下载图片");
///                        GetFile g = new GetFile(url, fileName);
///                        Thread t = new Thread(g);
///                        t.start();
///                        t.join(15000);
///                        t.interrupt();
///                        if (g.getStat() == 1) {
///                            String CQCode = CQ.getImage(fileName).toString();
///                            System.out.println("准备添加R18色图, 当前队列内有: " + setu.size() + "张图片");
///                            setuR18.put(CQCode);
///                            System.out.println("已将R18CQ码添加至队列, 耗时: " + (System.currentTimeMillis() - start));
///                        } else {
///                            System.out.println("获取失败");
///                            GetFile g1 = new GetFile(url, fileName);
///                            Thread t1 = new Thread(g);
///                            t1.start();
///                            t1.join(15000);
///                            t1.interrupt();
///                            if (g1.getStat() == 1) {
///                                String CQCode = CQ.getImage(fileName).toString();
///                                System.out.println("准备添加R18色图, 当前队列内有: " + setu.size() + "张图片");
///                                setuR18.put(CQCode);
///                                System.out.println("已将R18CQ码添加至队列, 耗时: " + (System.currentTimeMillis() - start));
///                            }
///                        }
///                    } else if (json.getInteger("code") == 429) {
///                        String path = CQ.getCQPath() + "/data/image/";
///                        File file = new File(path);
///                        File[] files = file.listFiles((dir, name) -> {
///                            try {
///                                if (name.startsWith("R18-")) {
///                                    Integer.valueOf(name.substring(4));
///                                    return true;
///                                }
///                                return false;
///                            } catch (Exception e) {
///                                return false;
///                            }
///                        });
///                        assert files != null;
///                        System.out.println(files.length);
///                        if (files.length == 0) {
///                            setuR18.put("0000");
///                            return;
///                        }
///                        int index = new Random().nextInt(files.length);
///                        File imageFile = files[index];
///                        if (imageFile.length() == 0) {
///                            imageFile.delete();
///                            index = new Random().nextInt(files.length);
///                            imageFile = files[index];
///                        }
///                        InputStream is = new FileInputStream(imageFile);
///                        resizeImage(is, imageFile.getPath(), 999, "png");
///                        String fileName = imageFile.getName();
///                        System.out.println("图片文件名" + fileName);
///                        String CQCode = CQ.getImage(fileName).toString();
///                        System.out.println("准备添加");
///                        setuR18.put(CQCode);
///                        System.out.println("R18图片路径: " + path + fileName);
///                    }
///                } catch (Exception e) {
/////                        System.out.println("添加队列出现错误: " + e.getMessage());
///                    e.printStackTrace();
///                }
///            }
///        }).start();
///        new Thread(() -> {
///            while (true) {
///                System.out.println("开始获取图片");
///                long start = System.currentTimeMillis();
///                try {
///                    JSONObject json = getJson("0");
///                    if (json == null) {
///                        json = getJson("0");
///                    }
///                    System.out.println("获取到json数据: " + json);
///                    if (json.getInteger("code") == 0) {
///                        JSONObject data = JSONObject.parseObject(json.getJSONArray("data").getString(0));
///                        String url = data.getString("url");
///                        String fileName = data.getString("pid");
///                        System.out.println("准备下载图片");
///                        GetFile g = new GetFile(url, fileName);
///                        Thread t = new Thread(g);
///                        t.start();
///                        t.join(15000);
///                        t.interrupt();
///                        if (g.getStat() == 1) {
///                            String CQCode = CQ.getImage(fileName).toString();
///                            setu.put(CQCode);
///                            System.out.println("已将CQ码添加至队列, 耗时: " + (System.currentTimeMillis() - start) + ", 当前队列内有: " + setu.size() + "张图片");
///                        } else {
///                            System.out.println("获取失败");
///                            GetFile g1 = new GetFile(url, fileName);
///                            Thread t1 = new Thread(g);
///                            t1.start();
///                            t1.join(15000);
///                            t1.interrupt();
///                            if (g1.getStat() == 1) {
///                                String CQCode = CQ.getImage(fileName).toString();
///                                setuR18.put(CQCode);
///                                System.out.println("已将R18CQ码添加至队列, 耗时: " + (System.currentTimeMillis() - start) + ", 当前队列内有: " + setuR18.size() + "张图片");
///                            }
///                        }
///                    } else if (json.getInteger("code") == 429) {
///                        String path = CQ.getCQPath() + "/data/image/";
///                        File file = new File(path);
///                        File[] files = file.listFiles((dir, name) -> {
///                            try {
///                                Integer.valueOf(name);
///                                return true;
///                            } catch (Exception e) {
///                                return false;
///                            }
///                        });
///                        assert files != null;
///                        if (files.length == 0) {
///                            setu.put("0000");
///                            return;
///                        }
///                        int index = new Random().nextInt(files.length);
///                        File imageFile = files[index];
///                        if (imageFile.length() == 0) {
///                            imageFile.delete();
///                            index = new Random().nextInt(files.length);
///                            imageFile = files[index];
///                        }
///                        InputStream is = new FileInputStream(imageFile);
///                        resizeImage(is, imageFile.getPath(), 999, "png");
///                        String fileName = imageFile.getName();
///                        System.out.println("图片文件名" + fileName);
///                        String CQCode = CQ.getImage(fileName).toString();
///                        System.out.println("准备添加");
///                        setu.put(CQCode);
///                        System.out.println("图片路径: " + path + fileName);
///                    }
///                } catch (Exception e) {
/////						System.out.println("添加队列出现错误: " + e.getMessage());
///                    e.printStackTrace();
///                }
///            }
///        }).start();
///    }

///    private JSONObject getJson(String r18) {
///        JSONObject json = null;
///        try {
///            String key1 = "820458705ebe071883b3c2";
///            String key2 = "198111555ec3242d2c6b42";
///            String web = Jsoup.connect("http://api.lolicon.app/setu/").ignoreContentType(true).data("apikey", key1)
///                    .data("size1200", "true").data("r18", r18).get().text();
///            json = JSONObject.parseObject(web);
///            if (json.getInteger("code") == 429) {
///                web = Jsoup.connect("http://api.lolicon.app/setu/").ignoreContentType(true).data("apikey", key2)
///                        .data("size1200", "true").data("r18", r18).get().text();
///                json = JSONObject.parseObject(web);
///            }
///        } catch (ConnectException e) {
///            System.out.println("获取json数据出现错误" + e.getMessage());
///        } catch (IOException e) {
///            e.printStackTrace();
///        }
///        System.out.println("获取到json数据: " + json);
///        return json;
///    }

    private void resizeImage(InputStream is, String outFileName) throws IOException {
        BufferedImage prevImage = ImageIO.read(is);
        double width = prevImage.getWidth();
        double height = prevImage.getHeight();
        double percent = 1000 / width;
        int newWidth = (int) (width * percent);
        int newHeight = (int) (height * percent);
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
        FileOutputStream out = new FileOutputStream(outFileName);
        System.out.println(outFileName);
        ImageIO.write(image, "png", out);
        out.flush();
        is.close();
        out.close();
    }

    class GetFile implements Runnable {
        final String url, fileName;
        int stat;

        public GetFile(String url, String fileName) {
            this.url = url;
            this.fileName = fileName;
        }

        @Override
        public void run() {

            try {
                URL url1 = new URL(url);
                URLConnection uc = url1.openConnection();
                uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = uc.getInputStream();
                String path = CQ.getCQPath() + "/data/image/";
                System.out.println("准备下载图片 " + path);
                long start = System.currentTimeMillis();
                FileOutputStream out = new FileOutputStream(path + fileName + ".temp");
                byte[] b = new byte[1024 * 1024];
                int len;
                while ((len = inputStream.read(b)) > 0) {
                    out.write(b, 0, len);
                }
                inputStream.close();
                out.close();
                InputStream inputStream2 = new FileInputStream(path + fileName + ".temp");
                resizeImage(inputStream2, path + fileName);
                System.out.println("已下载图片, 耗时: " + (System.currentTimeMillis() - start));
                this.stat = 1;
            } catch (Exception e) {
                System.out.println("已停止线程");
            }
        }

        public int getStat() {
            return stat;
        }

    }

}
