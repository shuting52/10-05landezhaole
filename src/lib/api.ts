import {
  activityLogs,
  buttons,
  cards,
  categories,
  operationLogs,
  statItems,
  texts,
} from "@/data/mockData";
import type {
  ActivityLog,
  CardQuery,
  Category,
  Paginated,
  ResourceButton,
  ResourceCard,
  StatItem,
  TextItem,
} from "@/types";
import { sleep } from "./utils";

/**
 * 数据访问层。
 * 目前全部读取本地 mock 数据，并模拟网络延迟。
 * 未来接入真实后端时，只需把每个函数体替换为 fetch/axios 调用，
 * 保持函数签名不变，页面组件无需改动。
 */

const LATENCY = 320;

export const api = {
  async getStats(): Promise<StatItem[]> {
    await sleep(LATENCY);
    return statItems;
  },

  async getActivityLogs(): Promise<ActivityLog[]> {
    await sleep(LATENCY);
    return activityLogs;
  },

  async getOperationLogs(): Promise<ActivityLog[]> {
    await sleep(LATENCY);
    return operationLogs;
  },

  async getCards(query: CardQuery = {}): Promise<Paginated<ResourceCard>> {
    await sleep(LATENCY);
    const { keyword = "", status = "all", category = "all", page = 1, pageSize = 6 } = query;
    const kw = keyword.trim().toLowerCase();

    let list = cards.filter((c) => {
      const matchKeyword =
        !kw ||
        c.name.toLowerCase().includes(kw) ||
        c.description.toLowerCase().includes(kw) ||
        c.category.toLowerCase().includes(kw);
      const matchStatus = status === "all" || c.status === status;
      const matchCategory = category === "all" || c.category === category;
      return matchKeyword && matchStatus && matchCategory;
    });

    const total = list.length;
    const start = (page - 1) * pageSize;
    list = list.slice(start, start + pageSize);

    return { items: list, total, page, pageSize };
  },

  async getAllCards(): Promise<ResourceCard[]> {
    await sleep(LATENCY);
    return cards;
  },

  async getButtons(): Promise<ResourceButton[]> {
    await sleep(LATENCY);
    return buttons;
  },

  async getTexts(): Promise<TextItem[]> {
    await sleep(LATENCY);
    return texts;
  },

  async getCategories(): Promise<Category[]> {
    await sleep(LATENCY);
    return categories;
  },
};
