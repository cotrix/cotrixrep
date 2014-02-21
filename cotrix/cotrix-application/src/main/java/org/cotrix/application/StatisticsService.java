package org.cotrix.application;

public interface StatisticsService {
	
	
	Statistics statistics();
	
	public static class Statistics {

		private final int codelists;
		private final int codes;
		private final int users;
		private final int repositories;
	
		public Statistics(int codelists, int codes, int users,int repositories) {
			this.codelists = codelists;
			this.codes = codes;
			this.users=users;
			this.repositories=repositories;
		}
		
		public int totalCodelists() {
			return codelists;
		}
		
		public int totalCodes() {
			return codes;
		}
		
		public int totalUsers() {
			return users;
		}
		
		public int totalRepositories() {
			return repositories;
		}

		@Override
		public String toString() {
			return "Statistics [codelists=" + codelists + ", codes=" + codes + ", users=" + users + ", repositories="
					+ repositories + "]";
		}
		
		
	}
	
	
}
