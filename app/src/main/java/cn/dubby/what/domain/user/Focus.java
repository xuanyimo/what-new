package cn.dubby.what.domain.user;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;

/**
 * Created by bixiaofeng on 16/5/16.
 */
@Table(name = "focus", id = ActiveAndroidConstant.ID)
public class Focus extends BaseEntity {
    @Column(name = "from_id")
    public Long fromId;

    @Column(name = "focus_id")
    public Long focusId;

}
