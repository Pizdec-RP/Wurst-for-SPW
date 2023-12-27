package net.purefps.modules;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.purefps.ai.PathFinder;
import net.purefps.ai.PathProcessor;
import net.purefps.commands.PathCmd;
import net.purefps.events.RenderListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.settings.BlockListSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.TextFieldSetting;
import net.purefps.util.BlockBreaker;
import net.purefps.util.BlockUtils;
import net.purefps.util.ChatUtils;

public class AutoMineAIHack extends Hack implements UpdateListener, RenderListener {//139 70 78 154 74 97

	private TextFieldSetting border = new TextFieldSetting("border", "minx miny minz maxx maxy maxz", "0 0 0 0 0 0");
	private SliderSetting range = new SliderSetting("range to block", "", 4.0, 0.0, 15.0, 1.0, SliderSetting.ValueDisplay.INTEGER.withLabel(0.0, "min"));
	private int minx, miny, minz, maxx, maxy, maxz;
	private List<BlockPos> tomine = new CopyOnWriteArrayList<>();
	private final BlockListSetting blocks = new BlockListSetting("Blocks to mine", "");
	private PathFinder pf;
	private Action a = Action.idle;
	private PathProcessor processor;

	enum Action {
		idle, going, mining, pathing
	}

	public AutoMineAIHack() {
		super("AutoMineAIHack");
		addSetting(border);
		addSetting(blocks);
		addSetting(range);
	}
	private String state = "no";
	@Override
	public String getRenderName() {
		return this.getName()+"["+a.toString()+" "+state+"]";
	}

	public void setState(String s) {
		state = s;
	}

	public static double sqrt(Vec3d one, BlockPos two) {
		return Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2) + Math.pow(one.getZ() - two.getZ(), 2));
	}

	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks) {
		if (pf == null) return;
		PathCmd pathCmd = WURST.getCmds().pathCmd;
		pf.renderPath(matrixStack, pathCmd.isDebugMode(), pathCmd.isDepthTest());
	}

	@Override
	public void onEnable() {
		try {
			/*BlockPos cur = MC.player.getBlockPos();
			if (BlockUtils.getName(cur).contains("air") && BlockUtils.getName(cur.up()).contains("air")) {//can stay in
				String m1 = BlockUtils.getName(cur.down());
				if (!m1.contains("air") && !m1.contains("water") && !m1.contains("lava")) {//it is normal block
					ChatUtils.message("can");
				} else {
					ChatUtils.message("cant1");
				}
			} else {
				ChatUtils.message("cant2");
			}
			setEnabled(false);*/
			tomine.clear();
			String[] split = border.getValue().split(" ");
			minx = (int)Math.floor(Double.parseDouble(split[0]));
			miny = (int)Math.floor(Double.parseDouble(split[1]));
			minz = (int)Math.floor(Double.parseDouble(split[2]));
			maxx = (int)Math.floor(Double.parseDouble(split[3]));
			maxy = (int)Math.floor(Double.parseDouble(split[4]));
			maxz = (int)Math.floor(Double.parseDouble(split[5]));
			setState("searching..");
			if (blocks.getBlockNames().size() == 0) setEnabled(false);
			for (int x = Math.min(minx, maxx); x < Math.max(minx, maxx); x++) {
				for (int y = Math.min(miny, maxy); y < Math.max(miny, maxy); y++) {
					for (int z = Math.min(minz, maxz); z < Math.max(minz, maxz); z++) {
						BlockPos cur = new BlockPos(x,y,z);
						String curname = BlockUtils.getName(cur);
						for (String name : blocks.getBlockNames()) {
							if (curname.equalsIgnoreCase(name)) {
								tomine.add(cur);
							}
						}
					}
				}
			}
			setState("done!");
			a = Action.idle;
			if (tomine.size() == 0) setEnabled(false);
			EVENTS.add(UpdateListener.class, this);
			EVENTS.add(RenderListener.class, this);
		} catch (Exception e) {
			ChatUtils.warning(e.getMessage());
			this.setEnabled(false);
		}
    }

    @Override
	public void onDisable() {
    	EVENTS.remove(UpdateListener.class, this);
    	EVENTS.remove(RenderListener.class, this);
    	PathProcessor.lockControls();
    	a = Action.idle;
    	tm = null;
    	tomine.clear();
    	pf = null;
    	processor = null;
    }

    private static final BlockPos[] sides = new BlockPos[] {
    	new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0),
    	new BlockPos(1, 0, 1), new BlockPos(-1, 0, -1), new BlockPos(1, 0, -1), new BlockPos(-1, 0, 1),

    	new BlockPos(1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1), new BlockPos(-1, 1, 0),
    	new BlockPos(1, 1, 1), new BlockPos(-1, 1, -1), new BlockPos(1, 1, -1), new BlockPos(-1, 1, 1),

    	new BlockPos(1, -1, 0), new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(-1, -1, 0),
    	new BlockPos(1, -1, 1), new BlockPos(-1, -1, -1), new BlockPos(1, -1, -1), new BlockPos(-1, -1, 1),
    };

    BlockPos tm;
	@Override
	public void onUpdate() {
		if (a == Action.idle) {
			if (tomine.size() == 0) {
				setEnabled(false);
				return;
			}
			setState("prc "+tomine.size());
			tm = tomine.get(0);
			for (BlockPos temp : tomine) {
				if (sqrt(MC.player.getPos(), tm) > sqrt(MC.player.getPos(), temp)) {
					tm = temp;
				}
			}
			tomine.remove(tm);
			if (sqrt(MC.player.getEyePos(), tm) <= range.getValue()) {
				a = Action.mining;
			} else {
				BlockPos wherestand = null;
				for (BlockPos bb : sides) {
					//ChatUtils.message(bb.toString());
					BlockPos cur = new BlockPos(tm.getX() + bb.getX(), tm.getY()+bb.getY(), tm.getZ()+bb.getZ());
					if (BlockUtils.getName(cur).contains("air") && BlockUtils.getName(cur.up()).contains("air")) {//can stay in
						String m1 = BlockUtils.getName(cur.down());
						if (!m1.contains("air") && !m1.contains("water") && !m1.contains("lava")) {//it is normal block
							if (wherestand == null) {
								wherestand = cur;
							} else if (sqrt(MC.player.getPos(), wherestand) > sqrt(MC.player.getPos(), cur)) {
								wherestand = cur;
							}
						}
					}
				}
				if (wherestand == null) {
					//ChatUtils.message("huyma");
					return;
				}
				pf = new PathFinder(wherestand);
				a = Action.pathing;
			}
		} else if (a == Action.pathing) {
			if(!pf.isDone()) {
				PathProcessor.lockControls();
				pf.think();
				if(!pf.isDone()) {
					if(pf.isFailed()) {
						setState("Could not find a path.");
						a = Action.idle;
					}
					return;
				}
				pf.formatPath();
				processor = pf.getProcessor();
				setState("Done");
				a = Action.going;
			}
		} else if (a == Action.going) {
			setState("going");
			processor.process();

			if(processor.isDone()) {
				PathProcessor.lockControls();
				a = Action.mining;
			}
		} else if (a == Action.mining) {
			BlockBreaker.breakOneBlockLegit(tm);
			if (BlockUtils.getName(tm).contains("air")) {
				a = Action.idle;
				setState("mined "+tm.toString());
			}
		}
	}

}
