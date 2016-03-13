package sonar.logistics.registries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.utils.BlockCoords;
import sonar.logistics.Logistics;
import sonar.logistics.api.cache.CacheTypes;
import sonar.logistics.api.cache.EmptyNetworkCache;
import sonar.logistics.api.cache.INetworkCache;
import sonar.logistics.cache.NetworkCache;

public final class CacheRegistry {

	public static final EmptyNetworkCache EMPTY_CACHE = new EmptyNetworkCache();

	private static LinkedHashMap<Integer, INetworkCache> cache = new LinkedHashMap<Integer, INetworkCache>();

	public static void removeAll() {
		cache.clear();
	}

	public static Entry<BlockCoords, ForgeDirection> getFirstConnection(int networkID) {
		INetworkCache network = getCache(networkID);
		if (network != null) {
			return network.getExternalBlock(true);
		} else
			return null;
	}

	public static ArrayList<BlockCoords> getCacheList(CacheTypes type, int networkID) {
		INetworkCache network = getCache(networkID);
		if (network != null)
			return network.getConnections(type, true);
		else
			return new ArrayList();
	}

	public static LinkedHashMap<BlockCoords, ForgeDirection> getChannelArray(int networkID) {
		INetworkCache network = getCache(networkID);
		if (network != null)
			return network.getExternalBlocks(true);
		else
			return new LinkedHashMap();
	}

	public static INetworkCache getCache(int networkID) {
		INetworkCache networkCache = cache.get(networkID);
		if (networkCache == null) {
			return EMPTY_CACHE;
		}
		return networkCache;
	}

	public static void refreshCache(int networkID) {
		INetworkCache networkCache = cache.get(networkID);
		if (networkCache != null && networkCache instanceof INetworkCache) {
			((NetworkCache) networkCache).refreshCache(networkID);
		}
		NetworkCache network = new NetworkCache();
		network.refreshCache(networkID);
		cache.put(networkID, network);
		Logistics.logger.debug("refreshed cache");
	}

	public static LinkedHashMap<Integer, INetworkCache> getNetworkCache() {
		return cache;
	}
}