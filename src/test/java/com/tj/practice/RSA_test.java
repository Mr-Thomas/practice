package com.tj.practice;

import com.alibaba.fastjson.JSON;
import com.tj.practice.common.Constant;
import com.tj.practice.common.util.RSA.RSAUtils;
import com.tj.practice.common.util.RSA.RandomUtils;
import com.tj.practice.common.util.RSA.SerializeUtil;
import com.tj.practice.model.PrivateKeyBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RSA_test {

    @Autowired
    private HttpServletRequest request;

    @Test
    public void testKey() throws Exception {
        Map<String, String> stringStringMap = generateKey();
        String string = JSON.toJSONString(stringStringMap);
        System.out.println(string);
        PrivateKeyBean attribute = (PrivateKeyBean) request.getSession().getAttribute(Constant.PRIVATE_KEY_ATTRIBUTE_NAME + "_"+stringStringMap.get("token"));
        System.out.println(attribute.getPrivateKeyObject());
    }

    /**
     * 生成密钥(添加私钥缓存并返回公钥)
     * @return 公钥map<String,String>  {"modulus":"...公钥串...","exponent":"..exponent..","token":"...token串.."}
     */
    public Map<String ,String> generateKey(){
        //生成密钥对
        KeyPair keyPair = RSAUtils.generateKeyPair();
        RSAPublicKey publicKey = RSAUtils.getPublicKey(keyPair);
        PrivateKey privateKey = RSAUtils.getPrivateKey(keyPair);
        //随机生成token
        String token = RandomUtils.nextToken();
        Map<String,String> keyMap = new HashMap<>();
        keyMap.put("modulus", Base64.getEncoder().encodeToString(publicKey.getEncoded()));//公钥串
        keyMap.put("exponent", Base64.getEncoder().encodeToString(publicKey.getPublicExponent().toByteArray()));//公钥exponent
        keyMap.put("token", token);//公钥串
        PrivateKeyBean pb = new PrivateKeyBean(Base64.getEncoder().encodeToString(SerializeUtil.serialize(privateKey))
                , Base64.getEncoder().encodeToString(SerializeUtil.serialize(publicKey)));
        HttpSession session = request.getSession();
        //存入session 【一般都是存入缓存】
        session.setAttribute(Constant.PRIVATE_KEY_ATTRIBUTE_NAME+"_"+token,pb);
        //设置有效时间
        // Session的默认失效时间是30分钟
        //设置单位为秒，设置为-1永不过期
        session.setMaxInactiveInterval(60*30);
        return keyMap;
    }

    /**
     * 解密参数
     * @param name 参数名称
     * @param String token
     * @return 解密内容
     */
    public String decryptParameter(String decryptVal, String token)throws Exception {
        if(StringUtils.isBlank(token)) {
            throw new Exception("token参数为空");
        }
        if (StringUtils.isBlank(decryptVal)) {
            throw new Exception("签名参数为空");
        }
        HttpSession session = request.getSession();
        PrivateKeyBean privateKeyBean = (PrivateKeyBean)session.getAttribute(Constant.PRIVATE_KEY_ATTRIBUTE_NAME+"_"+token);
        RSAPrivateKey privateKey = (RSAPrivateKey) SerializeUtil.unserialize(Base64.getDecoder().decode((privateKeyBean.getPrivateKeyObject())));
        if(privateKey == null) {
            removePrivateKey(token);
            throw new Exception("RSA签名私钥为空");
        }

        try {
            return RSAUtils.decrypt(privateKey, decryptVal);
        } catch (UnsupportedEncodingException e) {
            log.error("请求参数验签失败：",e);
            throw new Exception("请求参数验签失败");
        }
    }

    /**
     * 移除私钥
     * @param String token
     */
    public void removePrivateKey(String token)throws Exception {
        if(StringUtils.isBlank(token)) {
            throw new Exception("token参数为空");
        }
        HttpSession session = request.getSession();
        session.removeAttribute(Constant.PRIVATE_KEY_ATTRIBUTE_NAME+"_"+token);
    }
}
