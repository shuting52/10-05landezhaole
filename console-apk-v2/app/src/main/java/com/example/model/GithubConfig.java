package com.example.model;

import com.example.data.AdminRepository;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B%\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\u0016"}, d2 = {"Lcom/example/model/GithubConfig;", "", "owner", "", "repo", "branch", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getOwner", "()Ljava/lang/String;", "getRepo", "getBranch", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class GithubConfig {
    public static final int $stable = 0;
    private final String branch;
    private final String owner;
    private final String repo;

    public GithubConfig() {
        this(null, null, null, 7, null);
    }

    public static /* synthetic */ GithubConfig copy$default(GithubConfig githubConfig, String str, String str2, String str3, int i, Object obj) {
        if ((i & 1) != 0) {
            str = githubConfig.owner;
        }
        if ((i & 2) != 0) {
            str2 = githubConfig.repo;
        }
        if ((i & 4) != 0) {
            str3 = githubConfig.branch;
        }
        return githubConfig.copy(str, str2, str3);
    }

    public final String component1() {
        return this.owner;
    }

    public final String component2() {
        return this.repo;
    }

    public final String component3() {
        return this.branch;
    }

    public final GithubConfig copy(String owner, String repo, String branch) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(repo, "repo");
        Intrinsics.checkNotNullParameter(branch, "branch");
        return new GithubConfig(owner, repo, branch);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GithubConfig) {
            GithubConfig githubConfig = (GithubConfig) obj;
            return Intrinsics.areEqual(this.owner, githubConfig.owner) && Intrinsics.areEqual(this.repo, githubConfig.repo) && Intrinsics.areEqual(this.branch, githubConfig.branch);
        }
        return false;
    }

    public int hashCode() {
        return (((this.owner.hashCode() * 31) + this.repo.hashCode()) * 31) + this.branch.hashCode();
    }

    public String toString() {
        String str = this.owner;
        String str2 = this.repo;
        return "GithubConfig(owner=" + str + ", repo=" + str2 + ", branch=" + this.branch + ")";
    }

    public GithubConfig(String owner, String repo, String branch) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(repo, "repo");
        Intrinsics.checkNotNullParameter(branch, "branch");
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
    }

    public /* synthetic */ GithubConfig(String str, String str2, String str3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? AdminRepository.DEFAULT_OWNER : str, (i & 2) != 0 ? AdminRepository.DEFAULT_REPO : str2, (i & 4) != 0 ? AdminRepository.DEFAULT_BRANCH : str3);
    }

    public final String getOwner() {
        return this.owner;
    }

    public final String getRepo() {
        return this.repo;
    }

    public final String getBranch() {
        return this.branch;
    }
}
