package cn.dubby.what.domain.circle;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cn.dubby.what.constant.ActiveAndroidConstant;
import cn.dubby.what.domain.base.BaseEntity;

/**
 * Created by Administrator on 2015/12/12.
 */
@Table(name = "resource", id = ActiveAndroidConstant.ID)
public class Resource extends BaseEntity {
	@Column(name = "tid")
	public Integer tid;

	@Column(name = "theme")
	public Theme theme;

	@Column(name = "path")
	public String path;

	//资源类型:1,video 2,image
	@Column(name = "type")
	public Integer type;

	@Column(name = "delete_flag")
	public Integer deleteFlag;
}
