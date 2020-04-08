package com.tj.practice;

/**
 * 此测试类用来测试Stream类的相关方法
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.tj.practice.model.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StreamTest {

    @Test
    public void testList() {
        List<Integer> list00 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> list11 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> collect = list11.stream().filter(a -> !list00.contains(a)).collect(Collectors.toList());
        System.out.println(collect);

    }

    /**
     * 如何把这个list转换成Map<Integer, User> 其中，key是user id，value是User对象
     */
    @Test
    public void testList2Map() {
        List<User> userList = Arrays.asList(new User(1, "张三"), new User(2, "李四"));
        log.error("userList={}", userList);
        List<String> stringList = Arrays.asList("aaa", "bbb", "baa", "ccc", "aac");
        log.error("stringList={}", stringList);
        int size = stringList.stream().filter(str -> str.contains("ddd")).collect(Collectors.toList()).size();
        log.error("size={}", size);
        List<String> strings = stringList.stream().filter(str -> str.contains("aa")).collect(Collectors.toList());
        log.error("strings={}", strings);
        Map<Integer, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        log.error("userMap={}", userMap);
        Map<Integer, User> userMap1 = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        log.error("userMap={}", userMap1);

        List<User> userList1 = Arrays.asList(new User(3, "张SI"), new User(4, "李WU"));
        Map<Integer, User> map = new HashMap<Integer, User>() {{
            userList.forEach(user -> {
                put(user.getId(), user);
            });
            userList1.forEach(user1 -> {
                put(user1.getId(), user1);
            });
        }};
//		ArrayList<User> users = new ArrayList<>(map.values());
        List<User> users = map.values().stream().collect(Collectors.toList());
        log.error("users={}", users);

        String str = "aaa,bbb,ccc,ddd";
        List<String> stringList1 = Arrays.asList(str.split(","));
        log.error("stringList1={}", stringList1);
        String str1 = stringList1.stream().map(item -> "'" + item + "'").collect(Collectors.joining(","));
        log.error("str1={}", str1);
    }

    /**
     * 把list中的每个map中的某个属性值取出来，转换成list<String>
     */
    @SuppressWarnings("serial")
    @Test
    public void testGetMapPropertyInMapList() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<String, String>() {{
            put("id", "id-1001");
            put("name", "张三");
        }};
        list.add(map1);

        Map<String, String> map2 = new HashMap<String, String>() {{
            put("id", "id-1002");
            put("name", "李四");
        }};
        list.add(map2);

        List<String> ids = list.stream().map(entity -> entity.get("id")).collect(Collectors.toList());
        List<String> names = list.stream().map(entity -> entity.get("name")).collect(Collectors.toList());
        log.error("Map中所有的id列表：{}", ids);
        log.error("Map中所有的name列表：{}", names);
    }

    @Test
    public void str2map() {
        String str = "k_no_order=DD2_201909207LCVXT265707136&k_oid_partner=SH100109&orderAmount=200.00&sign=1d646649a82112725f0a1da9369732da&status=1";
        Map<String, String> paramMap = Stream.of(str.split("&"))
                .map(obj -> obj.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry.length > 1 ? entry[1] : ""));
        log.error("结果map：{}", paramMap);
    }

    /**
     * 将URL参数串，转成map
     */
    @Test
    public void testUrl2Map() {
        String url = "https://cn.bing.com/search?q=eclipse字符错位&qs=n&form=QBRE&sp=-1&pq=eclipse字符错位&sc=0-12&sk=&cvid=777BD2711DD44752B58E893D4E4FF9DD";
        String paramStr = url.substring(url.indexOf("?") + 1);
        log.error("参数串：{}", paramStr);

        Map<String, String> paramMap = Stream.of(paramStr.split("&"))
                .map(obj -> obj.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry.length > 1 ? entry[1] : ""));
        log.error("结果map：{}", paramMap);

        //转json串
        String jsonStrStr = JSON.toJSONString(paramMap);
        log.error("jsonStrStr: {}", jsonStrStr);

        //base64加密
        String base64Str = java.util.Base64.getEncoder().encodeToString(jsonStrStr.getBytes(StandardCharsets.UTF_8));
        log.error("base64加密: {}", base64Str);
        //Base64数据特殊处理:“+”替换为“%2B”
        String replace = base64Str.replace("+", "%2B");
        log.error("replace: {}", replace);

        //base64解密
        String reqJson = new String(java.util.Base64.getDecoder().decode(base64Str.getBytes(StandardCharsets.UTF_8)));
        log.error("base64解密：{}", reqJson);
        //转json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonStrStr);
        log.error("jsonObject: {}", jsonObject);
    }

    /**
     * 将一个List<String>转为List<Integer>
     */
    @Test
    public void testStringList2IntegerList() {
        List<String> strList = Arrays.asList("100", "204", "300");
        log.error("字符串集合：{}", strList);
        List<Integer> intList = strList.stream().map(obj -> Integer.parseInt(obj)).collect(Collectors.toList());
        log.error("数字集合：{}", intList);
    }

    /**
     * filter用法
     */
    @Test
    public void test() {
        List<User> users = Arrays.asList(
                new User(1, "aaa", new BigDecimal(10)),
                new User(2, "bbb", new BigDecimal(10)),
                new User(3, "ccc", new BigDecimal(10)));
        List<User> collect = users.stream()
                .filter(user -> user.getName() != "aaa")
                .collect(Collectors.toList());
        log.info("collect: {}", collect);

        //先过滤 再转map
        Map<Integer, User> userMap = users.stream()
                .filter(user -> user.getName() != "aaa")
                .collect(Collectors.toMap(User::getId, user -> user));
        log.info("userMap: {}", userMap);

        boolean flag = users.stream()
                .filter(user -> user.getName().contains("ad"))
                .collect(Collectors.toList())
                .size() > 0;
        log.info("flag: {}", flag);   //false

        //sum求和
        int sum = users.stream()
                .filter(user -> user.getName() != "aaa")
                .mapToInt(User::getId)
                .sum();
        log.info("sum: {}", sum);  //5

        //parallel方法并行处理所有的task，并使用reduce方法计算最终的结果
        Integer reduce = users.stream()
                .parallel()
                .map(user -> user.getAmount().intValue())
                .reduce(0, Integer::sum);
        log.info("reduce: {}", reduce);  //30
    }

    /**
     * groupingBy分组
     */
    @Test
    public void test01() {
        List<User> users = Arrays.asList(new User(1, "xxx", User.Flag.MAN), new User(2, "yyy", User.Flag.WOMAN), new User(3, "zzz", User.Flag.MAN));
        Map<User.Flag, List<User>> collect = users.stream()
                .collect(Collectors.groupingBy(User::getFlag));
        log.info("collect: {}", collect);  //{WOMAN=[{id=2,name="yyy"}], MAN=[{id=1,name="xxx"}, {id=3,name="zzz"}]}
    }
}
