package cn.dubby.what.domain.user;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;

/**
 * Created by Administrator on 2015/12/12.
 */
@Table(name = "collect", id = ActiveAndroidConstant.ID)
public class Collect extends BaseEntity {
	@Column(name = "uid")
	public Long uid;

	@Column(name = "tid")
	public Long tid;

}
