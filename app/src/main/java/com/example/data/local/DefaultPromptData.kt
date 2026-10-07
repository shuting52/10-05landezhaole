package com.example.data.local

import com.example.R
import com.example.data.model.PromptItem
import com.example.data.model.PromptMediaType

object DefaultPromptData {
    val initialPrompts = listOf(
        // 1. Cyberpunk Megacity (Image)
        PromptItem(
            id = "prompt-cyberpunk-city",
            title = "未来赛博都市 · 霓虹飞车雨夜",
            promptText = "Cinematic shot of a sprawling futuristic cyberpunk mega city at midnight, flying vehicles with glowing neon trails, holographic billboards reflecting on wet rain streets, dense vertical architecture, ultra-detailed 8k resolution, volumetric atmospheric smoke, photorealistic octane render",
            negativePrompt = "blurry, low quality, distorted, oversaturated, deformed buildings, watermark, signature",
            chineseDesc = "赛博朋克立体都市雨夜全景，飞驰流光悬浮车与高耸全息霓虹招牌，电影质感光影与水汽倒影",
            mediaType = PromptMediaType.IMAGE,
            previewDrawableRes = R.drawable.img_preview_cyberpunk,
            targetModel = "Midjourney v6.1",
            aspectRatio = "16:9",
            category = "赛博科幻",
            parameters = "--ar 16:9 --v 6.1 --style raw --stylize 300",
            author = "CyberArt Lab",
            copyCount = 3820
        ),

        // 2. Hanfu Girl in Bamboo Forest (Image)
        PromptItem(
            id = "prompt-hanfu-portrait",
            title = "幽竹提灯 · 汉服古风唯美少女",
            promptText = "A breathtakingly beautiful young Asian woman in exquisite ethereal flowing emerald Hanfu silk dress, holding a delicate glowing lotus lantern, standing in a misty twilight bamboo forest, floating golden fireflies, volumetric soft moonlight, 85mm portrait lens photography, hyperrealistic skin pores, masterpiece",
            negativePrompt = "bad anatomy, extra fingers, cartoon, 3d render, plastic skin, mutation, blurry eyes",
            chineseDesc = "江南竹林薄雾暮色，汉服少女手持莲花暖光花灯，流萤点点与柔和月色，大师级胶片人像质感",
            mediaType = PromptMediaType.IMAGE,
            previewDrawableRes = R.drawable.img_preview_hanfu,
            targetModel = "FLUX.1-dev",
            aspectRatio = "3:4",
            category = "唯美写实",
            parameters = "--ar 3:4 --steps 30 --guidance 3.5",
            author = "国风美学社",
            copyCount = 4290
        ),

        // 3. Sora Golden Dragon Soaring (Video)
        PromptItem(
            id = "prompt-sora-dragon",
            title = "Sora 电影级 · 金龙穿云越峰长镜头",
            promptText = "Cinematic drone tracking shot: A majestic oriental golden dragon with shimmering scales and flowing whiskers soars through sea of clouds above dramatic misty mountain peaks, ancient cliffside pagodas below, sunset golden hour rays penetrating mist, smooth epic cinematic motion, photorealistic 4k movie footage",
            negativePrompt = "jerky motion, distorted dragon, glitch, CGI artifacts, low fps, plastic textures",
            chineseDesc = "【视频生成】Sora电影级镜头：万丈云海间东方金鳞神龙乘风掠过奇峰险峦，古刹悬阁隐于落日余晖中",
            mediaType = PromptMediaType.VIDEO,
            previewDrawableRes = R.drawable.img_preview_dragon,
            videoDuration = "0:08",
            targetModel = "OpenAI Sora",
            aspectRatio = "16:9",
            category = "电影视频",
            parameters = "--motion 7 --fps 60 --duration 8s --camera pan right up",
            author = "Sora Visionary",
            copyCount = 5820
        ),

        // 4. Cute 3D Cyber Mascot Robot (Image)
        PromptItem(
            id = "prompt-3d-robot",
            title = "可爱3D黏土宇航机甲 · IP潮玩设计",
            promptText = "Chibi cute futuristic astronaut robot mascot floating with colorful holographic data crystals, smooth matte clay texture, soft studio ambient occlusion lighting, clean pastel gradients, ray tracing, C4D and Blender 3D render, behance trending digital toy art",
            negativePrompt = "harsh shadows, noisy background, realistic human, terrifying, low poly, bad lighting",
            chineseDesc = "Q版赛博太空机甲小机器人，悬浮全息彩色数字水晶，磨砂黏土质感与柔光棚拍，3D潮玩盲盒风格",
            mediaType = PromptMediaType.IMAGE,
            previewDrawableRes = R.drawable.img_preview_mascot,
            targetModel = "Midjourney v6.1",
            aspectRatio = "1:1",
            category = "3D渲染",
            parameters = "--ar 1:1 --v 6.1 --stylize 150 --chaos 5",
            author = "潮玩三维实验室",
            copyCount = 2650
        ),

        // 5. Kling AI Neon Cyber Motorcycle Chase (Video)
        PromptItem(
            id = "prompt-kling-moto",
            title = "可灵AI · 暴雨夜未来霓虹机车狂飙",
            promptText = "Low-angle dynamic tracking camera chasing a futuristic sleek electric motorbike speeding through a neon-soaked cyberpunk highway in heavy rain, glowing wheels spraying water droplets, rear thruster flames, camera shaking with speed, realistic motion blur, 4K 60fps",
            negativePrompt = "static camera, freeze frame, blurry motorbike, unnatural turns, low frame rate",
            chineseDesc = "【视频生成】快手可灵AI：暴雨霓虹高架未来超导机车极速破空狂飙，后轮水雾飞溅与低角度动态追焦",
            mediaType = PromptMediaType.VIDEO,
            previewDrawableRes = R.drawable.img_preview_cyberpunk,
            videoDuration = "0:05",
            targetModel = "快手可灵 Kling AI",
            aspectRatio = "16:9",
            category = "电影视频",
            parameters = "--mode high_performance --camera follow rear --speed fast",
            author = "Kling 动力流",
            copyCount = 3120
        ),

        // 6. Runway Gen-3 Starship Hyperspace Jump (Video)
        PromptItem(
            id = "prompt-runway-space",
            title = "Runway Gen-3 · 巨型星舰曲率跃迁星海",
            promptText = "Epic sci-fi cinematic sequence: A kilometer-long interstellar battlecruiser emerges from dark asteroid belt, engine coils warming with brilliant cyan plasma glow, warp drive activates creating gravitational light distortion and bursts into hyperspace jump, starry galaxy background, 4K film grade",
            negativePrompt = "flat lighting, 2d cutouts, cartoon stars, distorted ship hull, sudden cuts",
            chineseDesc = "【视频生成】Runway Gen-3：千米级恒星巡洋舰穿越小行星带，青色等离子聚能引发空间引力透镜跃迁",
            mediaType = PromptMediaType.VIDEO,
            previewDrawableRes = R.drawable.img_preview_dragon,
            videoDuration = "0:10",
            targetModel = "Runway Gen-3 Alpha",
            aspectRatio = "16:9",
            category = "电影视频",
            parameters = "--motion 8 --camera forward push --upscale 4k",
            author = "DeepSpace Studio",
            copyCount = 4190
        ),

        // 7. Luma Dream Machine Lantern Festival (Video)
        PromptItem(
            id = "prompt-luma-festival",
            title = "Luma · 盛唐千灯万火夜宴游人如织",
            promptText = "First-person perspective walking forward through an ancient vibrant Tang Dynasty night market, thousands of red and gold silk lanterns floating into sky, fireworks bursting overhead, bustling cheerful crowds in traditional hanfu, steaming street food stalls, warm glowing lantern illumination, ultra-smooth motion",
            negativePrompt = "modern buildings, distorted faces, frozen people, creepy smile, dark muddy colors",
            chineseDesc = "【视频生成】Luma Dream Machine：第一人称沉浸式漫步盛唐上元灯会，万盏孔明灯升空与烟花盛景",
            mediaType = PromptMediaType.VIDEO,
            previewDrawableRes = R.drawable.img_preview_hanfu,
            videoDuration = "0:06",
            targetModel = "Luma Dream Machine",
            aspectRatio = "16:9",
            category = "唯美写实",
            parameters = "--camera slow forward --realism 1.2",
            author = "盛世华章",
            copyCount = 3380
        ),

        // 8. Minimalist Apple Style App Icon (Image)
        PromptItem(
            id = "prompt-apple-icon",
            title = "苹果风微立体 · 渐变磨砂玻璃图标",
            promptText = "Sleek minimalist mobile app icon design, geometric glowing brain core enveloped in translucent frosted glass layers, soft purple to energetic cyan gradient, smooth chamfered edges, subtle drop shadow, premium Apple design award aesthetic, pure dark grey background",
            negativePrompt = "complex text, messy details, pixelated, 3d clutter, low resolution",
            chineseDesc = "顶级苹果设计规范微立体App图标，半透明磨砂玻璃层包裹发光神经核芯，青紫光泽与倒角高光",
            mediaType = PromptMediaType.IMAGE,
            previewDrawableRes = R.drawable.img_preview_mascot,
            targetModel = "DALL-E 3",
            aspectRatio = "1:1",
            category = "3D渲染",
            parameters = "--v 6.0 --quality 2 --no text words",
            author = "UI/UX 精研社",
            copyCount = 2990
        )
    )
}
