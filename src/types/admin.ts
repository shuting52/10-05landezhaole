// ============================================================
// admin-data.json 真实结构类型（对接规范 V1 §4 数据字典）
// ============================================================

export interface AdminData {
  version: VersionDto;
  home: HomeDto;
  software: SoftwareDto[];
  skills: SkillDto[];
  settings: SettingsDto;
  splash: SplashDto;
  welcome: WelcomeDto;
  updateDialog: UpdateDialogDto;
  marquee: MarqueeDto;
  console: ConsoleDto;
  ipMonitor: IpMonitorDto;
  tools: ToolDto[];
  themeKit: ThemeKitDto;
  _ai_notice: string;
  uiText: Record<string, string>;
  uiTextDefs: UiTextDef[];
  autoRelease: AutoReleaseDto;
}

export interface VersionDto {
  code: number;
  name: string;
  changelog: string[];
  force: boolean;
  apkUrl: string;
  apkUrlRaw: string;
  lastBuildSha: string;
}

export interface HomeDto {
  categories: CategoryDto[];
}

export interface CategoryDto {
  id: string;
  name: string;
  iconKey: string;
  desc: string;
  subcategories: SubCategoryDto[];
  cards: CardDto[];
}

export interface SubCategoryDto {
  id: string;
  name: string;
}

export interface CardDto {
  id: string;
  title: string;
  url: string;
  icon: string;
  fallbackText: string;
  badge: string;
  badgeType: string;
  desc: string;
  categoryId: string;
  subcatId: string;
  highlights?: string;
  [k: string]: unknown;
}

export interface SoftwareDto {
  id: string;
  type: string;
  title: string;
  desc: string;
  url: string;
  author: string;
  badge: string;
  tags: string;
  apkUrl: string;
  previewUrl: string;
  iconUrl: string;
  mode: string;
  badgeType: string;
  [k: string]: unknown;
}

export interface SkillDto {
  id: string;
  type: string;
  promptType: string;
  title: string;
  desc: string;
  prompt: string;
  url: string;
  author: string;
  badge: string;
  tags: string;
  previewUrl: string;
  mediaUrl: string;
  iconUrl: string;
  mode: string;
  [k: string]: unknown;
}

export interface SettingsDto {
  appName: string;
  slogan: string;
  aboutText: string;
  contactQQ: string;
  contactWechat: string;
  contactAlipay: string;
  qqGroupUrl: string;
  qqGroupUin: string;
  officialWebsite: string;
  feedbackEmail: string;
  customThemeCss: string;
  customThemeHtml: string;
  logoUrl: string;
  packageName: string;
  security: SecurityDto;
  serverShutdown: ServerShutdownDto;
  themeKit?: unknown;
  [k: string]: unknown;
}

export interface SecurityDto {
  enabled: boolean;
  expectedSha: string;
}

export interface ServerShutdownDto {
  enabled: boolean;
  notice: string;
}

export interface SplashDto {
  type: string;
  customHtml: string;
  mediaUrl: string;
  durationSeconds: number;
  bgColor: string;
}

export interface WelcomeDto {
  enabled: boolean;
  title: string;
  welcomeText: string;
  content: string;
  ratio: string;
  imageUrl: string;
  buttonText: string;
}

export interface UpdateDialogDto {
  title: string;
  changelog: string[];
  confirmText: string;
  cancelText: string;
  customCss: string;
  customHtml: string;
}

export interface MarqueeDto {
  enabled: boolean;
  icon: string;
  defaultText: string;
  segments: MarqueeSegmentDto[];
}

export interface MarqueeSegmentDto {
  text: string;
  [k: string]: unknown;
}

export interface ConsoleDto {
  version: string;
  code: number;
  apkUrl: string;
}

export interface IpMonitorDto {
  enabled: boolean;
  url: string;
}

export interface ToolDto {
  id: string;
  [k: string]: unknown;
}

export interface ThemeKitDto {
  appBar: { css: string };
  bottomBar: { css: string };
  splash: { css: string };
  statusBar: { css: string };
  card: { css: string };
  button: { css: string };
  dialog: { css: string };
  search: { css: string };
  global: { css: string };
  settingsPage: { css: string };
}

export interface UiTextDef {
  key: string;
  def: string;
  section: string;
  [k: string]: unknown;
}

export interface AutoReleaseDto {
  enabled: boolean;
  repo: string;
  workflow: string;
  mode: "content" | "release";
  lastRelease: {
    version: string;
    code: number;
    at: string;
    sha: string;
  };
}
