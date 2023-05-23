<template>
	<div class="footer">
		<p>&copy;2023-2024 By 也也</p>
		<a>xxxxxxxxxxxxxxxxxxx号</a>
	</div>
</template>

<script>
export default {
	name: 'Footer',
	data() {
		return {};
	}
};
</script>

<style lang="less" scoped>
.footer {
	height: 120px;
	padding: 40px 0;
	background-color: var(--pink);
  color: var(--common-color);
	font-size: 14px;
}
a {
	display: block;
	color: var(--common-color);
}
p,
a {
	height: 28px;
	text-align: center;
	line-height: 28px;
}
</style>
