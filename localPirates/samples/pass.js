var logic = {
	bid: function (data) {
		return {
			pass: true
		};
	},

	match_end: function (data, rl) {
		rl.close();
	}
};

var loop = require("./loop");
loop(logic);