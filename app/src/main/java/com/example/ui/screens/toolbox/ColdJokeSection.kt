package com.example.ui.screens.toolbox

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * v1.1.18 新工具：冷笑话
 *
 * - 内置 40+ 条冷笑话库，随机按钮一键生成
 * - 支持一键分享到聊天平台 / 短视频平台（系统分享面板，可发微信/QQ/抖音/微博等）
 */
private val ColdJokes = listOf(
    "为什么手机听歌要戴耳机？因为不戴耳机，手机以为自己在KTV。",
    "去面试，面试官问我有什么特长，我说我腿特长。",
    "为什么鱼从不说话？因为一开口就要被煲汤。",
    "你知道为什么秋天要喝奶茶吗？因为‘秋天第一杯奶茶’听起来比‘夏天第八杯奶茶’洋气。",
    "小明考试作弊被抓，老师问他为什么抄答案，他说：我只是在复习。",
    "为什么程序员分不清万圣节和圣诞节？因为 OCT 31 = DEC 25。",
    "有一次我摔倒了，爬起来拍拍身上的灰说：灰太狼都没我灰。",
    "为什么猫不玩捉迷藏？因为它一躲起来就真的找不到。",
    "去银行存钱，柜员问我：你确定是来存钱的吗？我说：你猜。",
    "为什么冬天的手机特别冷？因为它被冻得直哆嗦。",
    "我买了个会走路的闹钟，它每天自己走丢了。",
    "为什么数学书总是很忧郁？因为里面全是问题。",
    "熬夜对身体不好，所以我建议通宵。",
    "为什么蚊子不咬我？因为我已经被气饱了。",
    "熊猫的愿望是什么？拍一张彩色的照片。",
    "为什么乌鸦像写字台？因为我也不知道，但童话里都这么问。",
    "吃火锅的时候，毛肚和鸭肠谁更内卷？毛肚：我七上八下。",
    "为什么程序员的电脑总是很冷？因为里面有太多 CPU 风扇。",
    "医生对我说：你这病要戒酒、戒辣、戒熬夜。我问：那能戒饭吗？医生说：你想饿死我？",
    "为什么企鹅走路一摇一摆？因为它怕踩到自己的脚。",
    "我爸问我为什么不好好学习，我说：怕以后太优秀，配不上这世界。",
    "为什么说猪是吉祥物？因为‘猪’事顺利。",
    "我问冰箱有没有西瓜，冰箱说：你先把门关上。",
    "为什么汤圆那么圆？因为汤圆也想做‘圆’满的人。",
    "手机掉进水里怎么办？赶紧捞起来，不然水会进电。",
    "为什么月亮那么圆？因为它在努力‘圆’梦。",
    "坐公交车，售票员问我去哪，我说：去没烦恼的地方，他说：那你下车吧，前面就是。",
    "为什么说奶茶是快乐水？因为喝完快乐就‘吸’回来了。",
    "老师问：0.5 乘以 0.5 等于多少？小明：一半的一半。",
    "为什么仙人掌不交朋友？因为它扎手。",
    "我家的 WiFi 密码是我生日，结果邻居天天来蹭网祝我生日快乐。",
    "为什么奥特曼每次打完怪兽都要回光之国？因为家里没网，只能回光之国蹭WiFi。",
    "为什么方便面总是一包一包卖？因为它很‘方便’。",
    "去动物园，长颈鹿问我脖子累不累，我说：你试试抬头看我。",
    "为什么扫地机器人不开演唱会？因为它只会扫地，不会‘扫’音。",
    "老板问我要不要加班，我说：加，加到世界和平。",
    "为什么闹钟叫不醒我？因为它不懂我‘梦想’的重量。",
    "感冒了，我打喷嚏，老婆说：有人在想你。我说：那为什么我鼻子都打红了？",
    "为什么考试前要拜孔子？因为拜了孔子，及格‘孔’易。",
    "我养了一只乌龟，它说我走得快，我很感动。",
    "为什么西瓜怕热？因为它‘瓜’了。",
    "今天吃泡面，想加个蛋，翻遍冰箱没有，于是对着泡面说了句：蛋蛋的忧伤。"
)

@Composable
fun ColdJokeSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var joke by remember { mutableStateOf(ColdJokes.random()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题卡
        Surface(
            color = Color(0xFFFFFDF9).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Color(0xFFFFB300).copy(alpha = 0.6f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("😂 冷笑话", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFFE8730A))
                Text("随机一条冷笑话 · 一键分享到聊天/短视频平台", fontSize = 11.sp, color = Color(0xFF8A6A3B), modifier = Modifier.padding(top = 2.dp))
            }
        }

        // 笑话卡片
        Surface(
            color = Color(0xFFFFFBF2),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, Color(0xFFFFD54F).copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 26.dp)
            ) {
                Text("❄️", fontSize = 26.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = joke,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4A2E14),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 随机 & 分享
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { joke = ColdJokes.random() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8730A)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.size(6.dp))
                Text("再来一条", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "【冷笑话】$joke\n—— 来自「懒得找了」App")
                    }
                    try {
                        context.startActivity(Intent.createChooser(send, "分享冷笑话到"))
                    } catch (e: Exception) {
                        Toast.makeText(context, "分享失败：${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Share, contentDescription = null)
                Spacer(modifier = Modifier.size(6.dp))
                Text("分享出去", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "分享面板会自动列出微信/QQ/抖音/微博等聊天与短视频平台，选一个就能发～",
            fontSize = 11.sp,
            color = Color(0xFF8A6A3B)
        )
    }
}
