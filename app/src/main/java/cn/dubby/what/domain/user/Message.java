package cn.dubby.what.domain.user;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;

/**
 * Created by Administrator on 2015/12/12.
 */
@Table(name = "message", id = ActiveAndroidConstant.ID)
public class Message extends BaseEntity {
	@Column(name = "from_id")
	public Long fromId;

	@Column(name = "to_id")
	public Long toId;

	@Column(name = "content")
	public String content;

}
