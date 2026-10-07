#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
合并新增站点 → admin-data.json
规则（对齐 写死规则.md 规则4）：
  · 只增不删：原有站点一律保留
  · 自动去重：规范化 URL 作为唯一键（去协议、去 www、去尾部斜杠、去查询参数）
  · 新增站点只追加到匹配分类的 cards 中
"""
import json, re, sys, os

BASE = os.path.dirname(os.path.abspath(__file__))
ADMIN = os.path.join(BASE, "admin-data.json")
BATCHES = [
    os.path.join(BASE, "sites_p1_ai.json"),
    os.path.join(BASE, "sites_p2_life.json"),
    os.path.join(BASE, "sites_p3_game.json"),
    os.path.join(BASE, "sites_p4_tools.json"),
]

def norm_url(u):
    if not u: return ""
    s = u.strip().lower()
    s = re.sub(r"^https?://", "", s)
    s = re.sub(r"^www\.", "", s)
    # 去掉路径与查询（按域名去重）
    s = re.sub(r"[?#].*$", "", s)
    s = re.sub(r"/+$", "", s)
    return s

def main():
    with open(ADMIN, "r", encoding="utf-8") as f:
        data = json.load(f)
    cats = data["home"]["categories"]

    # 构建分类索引：id -> obj
    cat_by_id = {c.get("id"): c for c in cats if isinstance(c, dict) and c.get("id")}
    # 现有 URL 集合（域名级别）
    seen = {}
    for c in cats:
        for card in c.get("cards", []):
            u = norm_url(card.get("url", ""))
            if u:
                seen.setdefault(u, card)

    added = 0
    skipped_dup = 0
    skipped_nocat = 0

    for bf in BATCHES:
        with open(bf, "r", encoding="utf-8") as f:
            batch = json.load(f)
        for cat_id, sites in batch.items():
            cat = cat_by_id.get(cat_id)
            if cat is None:
                skipped_nocat += len(sites)
                print(f"  [警告] 分类 {cat_id} 不存在，跳过 {len(sites)} 个站点")
                continue
            cards = cat.setdefault("cards", [])
            sub_ids = {s.get("id") for s in cat.get("subcategories", [])}
            for s in sites:
                url = s.get("url", "")
                nkey = norm_url(url)
                if not nkey:
                    continue
                if nkey in seen:
                    skipped_dup += 1
                    continue
                sub = s.get("subcatId", "all")
                if sub not in sub_ids:
                    sub = "all"
                card = {
                    "id": "site_add_" + str(len(seen) + added + 1),
                    "title": s.get("title", ""),
                    "url": url,
                    "icon": "",
                    "fallbackText": (s.get("title", "")[:2]).upper() if s.get("title") else "",
                    "badge": s.get("badge", ""),
                    "badgeType": s.get("badge", "NEW") if s.get("badge") else "",
                    "desc": s.get("desc", ""),
                    "categoryId": cat_id,
                    "subcatId": sub,
                    "highlights": "",
                }
                cards.append(card)
                seen[nkey] = card
                added += 1

    # 写回（保持缩进紧凑）
    with open(ADMIN, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=1)

    # 统计
    total = sum(len(c.get("cards", [])) for c in cats)
    print(f"完成：新增 {added}，跳过重复 {skipped_dup}，跳过无分类 {skipped_nocat}")
    print(f"当前站点总数：{total}，分类数：{len(cats)}")

if __name__ == "__main__":
    main()