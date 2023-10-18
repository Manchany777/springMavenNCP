package user.service;

import java.util.List;

import user.bean.UserImageDTO;

public interface UserService {
	public void upload(List<UserImageDTO> userImageList);

	public List<UserImageDTO> getUploadList();

}
