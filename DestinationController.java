package jp.co.internous.soleil.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.soleil.model.domain.MstDestination;
import jp.co.internous.soleil.model.domain.MstUser;
import jp.co.internous.soleil.model.form.DestinationForm;
import jp.co.internous.soleil.model.mapper.MstDestinationMapper;
import jp.co.internous.soleil.model.mapper.MstUserMapper;
import jp.co.internous.soleil.model.session.LoginSession;

/**
 * 宛先情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/soleil/destination")
public class DestinationController {
	
	/*
	 * フィールド定義
	 */
	@Autowired
	private MstDestinationMapper destinationMapper;
	@Autowired
	private MstUserMapper userMapper;
	@Autowired
	private LoginSession loginSession;
	
	private Gson gson = new Gson();

	/**
	 * 宛先画面を初期表示する (決済機能ページからのリクエストを受け取る)
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		MstUser user = userMapper.findByUserNameAndPassword(loginSession.getUserName(), loginSession.getPassword());

		m.addAttribute("user", user);
		m.addAttribute("loginSession", loginSession);
		
		return "destination";
	}
	
	/**
	 * 宛先情報を削除する
	 * @param destinationId 宛先情報ID
	 * @return true:削除成功、false:削除失敗
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/delete")
	@ResponseBody
	public boolean delete(@RequestBody String destinationId) {
		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");
		int result = destinationMapper.logicalDeleteById(Integer.parseInt(id));

		return result > 0;
	}
	
	/**
	 * 宛先情報を登録する
	 * @param f 宛先情報のフォーム
	 * @return 宛先情報id
	 */
	@PostMapping("/register")
	@ResponseBody
	public String register(@RequestBody DestinationForm f) {
		MstDestination destination = new MstDestination(f);
		int userId = loginSession.getUserId();
		destination.setUserId(userId);
		
		int count = destinationMapper.insert(destination);
		Integer id = count > 0 ? destination.getId() : 0;
		
		return id.toString();
	}
}
