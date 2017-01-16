var logic = {
	bid: function (data) {
		return { error: "lofasz" };
	},

	match_end: function (data, rl) {
		rl.close();
	}
};

var loop = require("./loop");
loop(logic);
