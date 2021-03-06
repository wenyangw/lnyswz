package lnyswz.jxc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.jxc.bean.Post;

public interface PostServiceI {

	public DataGrid datagrid(Post post);

	public Post add(Post post);

	public void edit(Post post);

	public void delete(Post post);
	
	public List<Post> listPosts(Post post);
}
