#### org/apache/commons/codec/binary/Hex.java

作用：做16进制字符与字节之间的转换。

使用：在加密、解密时经常需要将字节转为16进制的字符串后继续处理。

主要方法：encode、decode，其它方法都是这两个方法的重载或变形。

#### encode

输入数据：

> * 字节数组
> * 可转为字节数组的数据（字符串、ByteBuffer）

输出数据：

> * 0-9和a-f组成的字符数组

转换后的字符数组长度是原来字节数组长度的两倍（即，必定是偶数），因为转换过程是：

1. 将每个8位（bit）的字节转为高4位和低4位两个字节，使每个字节的值范围为[0，16)_（16=2^4^）。_
2. 然后根据此值作下标在下面字符数组（大写或小写）中取对应字符。

```{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}```

或

```{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}```

3. 返回由所有对应字符组成的字符数组。

##### 举例1：

```java
byte[] inputByteArray = new byte[]{(byte) 1, (byte) 20, (byte) 110, (byte) 242, (byte) -14};
System.out.println(Arrays.toString(encodeHex(inputByteArray)));
//结果：
[0, 1, 1, 4, 6, e, f, 2, f, 2]
```

解释：

前三个字节根据[ascii码表](https://baike.baidu.com/item/ASCII/309296#3)可知二进制如下：

* （byte）1，对应二进制0000 0001；高低四位的十进制分别是0、1；
* （byte）20，对应二进制0001 0100；高低四位的十进制分别是1、4；
* （byte）110，对应二进制0110 1110；高低四位的十进制分别是6、14；

后两个字节涉及负数：

* （byte）242，对应二进制为1111 0010；最高位为1，表示负数，实际值转补码后得-14：

  > 1. 按位取反：0000 1101
  > 2. 末位加一：0000 1110
  > 3. 高位置一：1000 1110

  值为-14，与最后一个字节的值一样，因此最后的输出也是一样的。

* （byte）-14，对应正数的二进制为0000 1110，转补码：

  > 1. 按位取反：1111 0001
  > 2. 末位加一：1111 0010
  > 3. 高位置一；1111 0010

  后两个字节在计算机里存储的值都是：1111 0010；高低四位的十进制分别是15、2。[关于2的补码](http://www.ruanyifeng.com/blog/2009/08/twos_complement.html)

因此，转换后所有字节的值是：[0, 1, 1, 4, 6, 14, 15, 2, 15, 2]，按下标对应数组元素，即为：[0, 1, 1, 4, 6, e, f, 2, f, 2]。输入5个字节，输出10个字符。

##### 举例2：

```java
System.out.println(Hex.encodeHexString("你好".getBytes("UTF-8")));
//结果：
e4bda0e5a5bd6e
```

解释：

与例1有两点不同：

> 1. 输入是字符串转换后的字节数组
> 2. 输出是字符串

1. 字符串（中文或英文）getBytes获取字节数组为：[-28, -67, -96, -27, -91, -67, 110]。
   其中『你』对应[-28, -67, -96]；『好』对应[-27, -91, -67]；『n』对应[110]。至于为什么在UTF-8编码下此字符串的字节是这个结果，另文再述。
2. 7个字节按照例1的步骤分别对应14个字符。
3. 将14个字符组成的数组转为字符串，即为上面结果。

#### decode

输入数据：

> 偶数个0-9和a-f的字符或字符串

输出数据：

> 字节数组

输出的字节数组长度是输入字符长度的$\frac{1}{2}$。因为转换过程是：

1. 将第一个字符对应的二进制左移4位后作为新字节的高四位。
2. 将第二个字符对应的二进制作为此新字节的低四位。（也就是两个字符合为一个字节）
3. 遍历所有字符做以上操作后，返回所有字节组成的数组。
4. 可尝试将此数组转为字符串。但不是所有字节数组都是可转为字符串。

##### 举例3

```java
String inputString = "e4bda0e5a5bd6e";
System.out.println("string length: "+inputString.length());
System.out.println(Arrays.toString(Hex.decodeHex(inputString)));
System.out.println(new String(Hex.decodeHex(inputString)));
//结果：
string length: 14
[-28, -67, -96, -27, -91, -67, 110]
你好n
```

解释：

上面例2的反向操作。

##### 举例4

```java
String inputString = "e4bda0e5a5bd6";
System.out.println("string length: " + inputString.length());
System.out.println(Arrays.toString(Hex.decodeHex(inputString)));
//结果：
string length: 13

Exception in thread "main" org.apache.commons.codec.DecoderException: Odd number of characters. 
```

解释：

奇数个字符是非法输入。

##### 举例5

```java
String inputString = "123xyz";
System.out.println("string length: " + inputString.length());
System.out.println(Arrays.toString(Hex.decodeHex(inputString)));
//结果：
string length: 6

Exception in thread "main" org.apache.commons.codec.DecoderException: Illegal hexadecimal character x at index 3
```

解释：

超出0-9和a-f范围的字符是非法输入。

##### 举例6

```java
String inputString = "123abc";
System.out.println("string length: "+inputString.length());
System.out.println(Arrays.toString(Hex.decodeHex(inputString)));
System.out.println(new String(Hex.decodeHex(inputString)));
//结果：
string length: 6
[18, 58, -68]
:�

```

解释：

并不是所有字节都可转为字符串。



（完）


